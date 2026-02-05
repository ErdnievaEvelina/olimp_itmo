package com.example.olimpitmo.data

import com.example.olimpitmo.data.remote.DatesMatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException

class MatchesRepositoryImpl(
    private val apiService: ApiService
): MatchesRepository {
    override suspend fun getListMatches(): Flow<Result<List<DatesMatch>>> {
        return flow {
            val matcheFromApi= try{
                apiService.getMatches()
            }catch (e: IOException){
                e.printStackTrace()
                emit(Result.Error(message="Error"))
                return@flow
            }catch (e: Exception){
                e.printStackTrace()
                emit(Result.Error(message="Error"))
                return@flow
            }
            emit(Result.Success(matcheFromApi.data))
        }
    }

    override suspend fun getListMatchesByLeagueDate(from: String): Flow<Result<List<DatesMatch>>> {
        return flow {
            val matchesFromApi= try{
                apiService.getMatchesByLeagueAndDate(from)
            }catch (e: IOException){
                e.printStackTrace()
                emit(Result.Error(message="Error"))
                return@flow
            }catch (e: Exception){
                e.printStackTrace()
                emit(Result.Error(message="Error"))
                return@flow
            }
            emit(Result.Success(matchesFromApi.data))
        }
    }


}
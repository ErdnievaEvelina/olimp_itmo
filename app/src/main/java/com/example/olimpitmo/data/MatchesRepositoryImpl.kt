package com.example.olimpitmo.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.olimpitmo.data.remote.DatesMatch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import java.time.LocalDate

class MatchesRepositoryImpl(
    private val apiService: ApiService
): MatchesRepository {

    override suspend fun getListMatches(): Flow<Result<List<DatesMatch>>> {
        return flow {
            try {
                val matchesFromApi = apiService.getMatches()
                emit(Result.Success(matchesFromApi.data))
            } catch (e: IOException) {
                emit(Result.Error(message = "Network error: ${e.message}"))
            } catch (e: Exception) {
                emit(Result.Error(message = "Unknown error: ${e.message}"))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getListMatchesByLeagueDate(from: String): Flow<Result<List<DatesMatch>>> {
        return flow {
            try {
                // Проверяем формат даты
                if (!isValidDate(from)) {
                    emit(Result.Error(message = "Invalid date format. Use YYYY-MM-DD"))
                    return@flow
                }

                val matchesFromApi = apiService.getMatchesByDate(from)
                emit(Result.Success(matchesFromApi.data))
            } catch (e: IOException) {
                emit(Result.Error(message = "Network error: ${e.message}"))
            } catch (e: Exception) {
                emit(Result.Error(message = "Unknown error: ${e.message}"))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCompletedMatches(from: String, to: String): Flow<Result<List<DatesMatch>>> {
        return flow {
            try {
                if (!isValidDate(from) || !isValidDate(to)) {
                    emit(Result.Error(message = "Invalid date format. Use YYYY-MM-DD"))
                    return@flow
                }

                if (LocalDate.parse(from).isAfter(LocalDate.parse(to))) {
                    emit(Result.Error(message = "Start date must be before end date"))
                    return@flow
                }

                val matchesFromApi = apiService.getCompletedMatches(from, to)
                emit(Result.Success(matchesFromApi.data))
            } catch (e: IOException) {
                emit(Result.Error(message = "Network error: ${e.message}"))
            } catch (e: Exception) {
                emit(Result.Error(message = "Unknown error: ${e.message}"))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun isValidDate(date: String): Boolean {
        return try {
            LocalDate.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }
}



package com.example.olimpitmo.data

import com.example.olimpitmo.data.remote.DatesMatch
import kotlinx.coroutines.flow.Flow

interface MatchesRepository {
    suspend fun getListMatches(): Flow<Result<List<DatesMatch>>>
    suspend fun getListMatchesByLeagueDate(from:String):Flow<Result<List<DatesMatch>>>
    suspend fun getCompletedMatches(from: String, to: String): Flow<Result<List<DatesMatch>>>
}
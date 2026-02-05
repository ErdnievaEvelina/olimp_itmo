package com.example.olimpitmo.data


import com.example.olimpitmo.data.remote.Matches
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("Games/list?from=2026-02-05&to=2026-02-07&apikey=f1svo803g0kuqvli")
    suspend fun getMatches(): Matches
    @GET("Games/list")
    suspend fun getMatchesByDate(
        @Query("from") from: String,
        @Query("apikey") apiKey: String = "f1svo803g0kuqvli"
    ): Matches
    @GET("Games/list")
    suspend fun getCompletedMatches(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("finished") finished: Boolean = true,
        @Query("apikey") apiKey: String = "f1svo803g0kuqvli"
    ): Matches
    companion object{
         const val URL="https://api.sstats.net/"
     }
}
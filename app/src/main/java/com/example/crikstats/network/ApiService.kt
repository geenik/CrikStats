package com.example.crikstats.network

import com.example.crikstats.data.PlayerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("/api/v2.0/players")
    suspend fun fetchData(
        @Query("api_token") apiKey: String,
    ): Response<PlayerResponse>
}
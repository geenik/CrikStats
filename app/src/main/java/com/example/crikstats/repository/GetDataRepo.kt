package com.example.crikstats.repository

import com.example.crikstats.constant.Constants
import com.example.crikstats.network.ApiService
import javax.inject.Inject

class GetDataRepo @Inject constructor(private val apiService: ApiService) {
    suspend fun getData() = apiService.fetchData(Constants.API_KEY)
}
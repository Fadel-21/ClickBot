package com.example.trackxmlapp.repository

import com.example.trackxmlapp.api.ApiClient
import com.example.trackxmlapp.api.ApiService
import com.example.trackxmlapp.models.RequestData

class Repository {
    private val apiService = ApiClient.retrofit.create(ApiService::class.java)

    suspend fun sendDataToServer(requestData: RequestData) {
        try {
            val response = apiService.sendData(requestData)
            if (!response.isSuccessful) {
                throw Exception("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw Exception("Failed to send data: ${e.message}")
        }
    }
}
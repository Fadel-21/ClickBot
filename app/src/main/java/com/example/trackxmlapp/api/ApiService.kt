package com.example.trackxmlapp.api

import com.example.trackxmlapp.models.RequestData
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("fadel")
    suspend fun sendData(@Body requestData: RequestData): Response<Void>
}
package com.example.sitanduapp.api

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("aarmeliaa/2f4584ff319191fb018bed04549c0b9a/raw/34227aa98f300901bb9d56c7aba64c8a88b4e4a5/request.json")
    fun getRequests(): Call<List<Request>>
}
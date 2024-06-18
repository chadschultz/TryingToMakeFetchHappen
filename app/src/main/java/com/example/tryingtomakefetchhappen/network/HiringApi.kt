package com.example.tryingtomakefetchhappen.network

import com.example.tryingtomakefetchhappen.model.HiringItem
import retrofit2.http.GET

interface HiringApi {
    @GET("hiring.json")
    suspend fun getHiringItems(): List<HiringItem>
}
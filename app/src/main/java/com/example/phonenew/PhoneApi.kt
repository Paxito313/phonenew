package com.example.realestateapp.network

import retrofit2.http.GET
import retrofit2.http.Path
import com.example.realestateapp.models.Phone

interface PhoneApi {
    @GET("products")
    suspend fun getPhones(): List<Phone>

    @GET("products/{id}")
    suspend fun getPhoneDetails(@Path("id") id: Int): Phone
}

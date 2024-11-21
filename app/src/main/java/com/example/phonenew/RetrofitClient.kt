package com.example.realestateapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.realestateapp.network.PhoneApi

object RetrofitClient {
    private const val BASE_URL = "https://my-json-server.typicode.com/Himuravidal/FakeAPIdata/" // Asegúrate de que esta URL es correcta

    val instance: PhoneApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(PhoneApi::class.java)
    }
}

package com.example.phonenew.repositories

import androidx.lifecycle.LiveData
import com.example.realestateapp.database.PhoneDao
import com.example.realestateapp.models.Phone
import com.example.realestateapp.network.PhoneApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataRepository(val phoneDao: PhoneDao, instance: PhoneApi) {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://my-json-server.typicode.com/Himuravidal/FakeAPIdata/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val phoneApi = retrofit.create(PhoneApi::class.java)

    val allPhones: LiveData<List<Phone>> = phoneDao.getAllPhones()

    suspend fun refreshPhones() {
        val phones = phoneApi.getPhones()
        phoneDao.insertAll(phones)
    }

    suspend fun getPhoneDetails(id: Int): Phone {
        return phoneApi.getPhoneDetails(id)
    }
}

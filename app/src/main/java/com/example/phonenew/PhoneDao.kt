package com.example.realestateapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.realestateapp.models.Phone

@Dao
interface PhoneDao {
    @Query("SELECT * FROM phone_table")
    fun getAllPhones(): LiveData<List<Phone>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(phones: List<Phone>)
}

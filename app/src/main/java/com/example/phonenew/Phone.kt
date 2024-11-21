package com.example.realestateapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phone_table")
data class Phone(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Int,
    val image: String,
    val credit: Boolean
)

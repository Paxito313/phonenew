package com.example.realestateapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.realestateapp.models.Phone

@Database(entities = [Phone::class], version = 2, exportSchema = false) // Incrementa la versión aquí
abstract class PhoneDatabase : RoomDatabase() {

    abstract fun phoneDao(): PhoneDao

    companion object {
        @Volatile
        private var INSTANCE: PhoneDatabase? = null

        fun getDatabase(context: Context): PhoneDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhoneDatabase::class.java,
                    "phone_database"
                ).fallbackToDestructiveMigration() // Esto elimina y vuelve a crear la base de datos si hay un cambio en el esquema
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

package com.wiuma.astraios.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wiuma.astraios.models.Horoscope

@Database(entities = [Horoscope::class], version = 1)
abstract class HoroscopeDatabase : RoomDatabase() {

    abstract fun horoscopeDao(): HoroscopeDAO

    companion object {
        @Volatile
        private var instance: HoroscopeDatabase? = null
        private val lock = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext, HoroscopeDatabase::class.java, "horoscopeDatabase").build()
    }
}
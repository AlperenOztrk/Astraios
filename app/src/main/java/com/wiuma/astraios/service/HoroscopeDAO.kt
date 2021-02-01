package com.wiuma.astraios.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wiuma.astraios.models.Horoscope

@Dao
interface HoroscopeDAO {
    @Insert
    suspend fun insertAll(vararg horoscope: Horoscope): List<Long>

    @Query("SELECT * from horoscope")
    suspend fun getAll(): List<Horoscope>

    @Query("SELECT * from horoscope WHERE Id = :horoscopeId")
    suspend fun getHoroscope(horoscopeId : Int) : Horoscope

    @Query("DELETE FROM horoscope")
    suspend fun deleteAll()
}
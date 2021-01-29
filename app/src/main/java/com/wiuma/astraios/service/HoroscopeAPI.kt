package com.wiuma.astraios.service

import com.wiuma.astraios.models.Horoscope
import io.reactivex.Single
import retrofit2.http.GET

interface HoroscopeAPI {
    @GET("astraios/horoscopeList.json")
    fun getHoroscope(): Single<List<Horoscope>>
}
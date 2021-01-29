package com.wiuma.astraios.service

import com.wiuma.astraios.models.Horoscope
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HoroscopeAPIService {
    private val BASE_URL = "https://wiuma.co"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(HoroscopeAPI::class.java)

    fun getData(): Single<List<Horoscope>> {
        return api.getHoroscope()
    }
}
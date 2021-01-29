package com.wiuma.astraios.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wiuma.astraios.models.Horoscope
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {
    val horoscopeList = MutableLiveData<List<Horoscope>>()
    val returnBoolean = MutableLiveData<Boolean>()
    val loadingStatus = MutableLiveData<Boolean>()
    val date = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val usersHoroscopeId = MutableLiveData<Int>()

    fun refreshData() {
        //val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val sdf = SimpleDateFormat.getDateInstance()
        val currentDate = sdf.format(Date())
        val nameFromData = "Alperen"
        val usernameFromData = "AlpOzturk"
        val horoscopeIdFromData = 2
        date.value = currentDate
        username.value = usernameFromData
        name.value = nameFromData
        usersHoroscopeId.value = horoscopeIdFromData
        returnBoolean.value = true
        loadingStatus.value = false
        val piscis = Horoscope(
            2,
            "Balık",
            "https://wiuma.co/astraios/sign/scorpio.png",
            "https://wiuma.co/astraios/sign/scorpio.png",
            "Günlük yorumu",
            "25 Şubat",
            "26 Mart",
            "Mars",
            "https://wiuma.co/astraios/sign/scorpio.png",
            "Ateş",
            "https://wiuma.co/astraios/element/fire.png",
            "Karekteristik Özellikleri",
            "https://wiuma.co/astraios/inksign/scorpio.png"
        )
        val horoscopes = arrayListOf<Horoscope>(piscis)
        horoscopeList.value = horoscopes
    }
}
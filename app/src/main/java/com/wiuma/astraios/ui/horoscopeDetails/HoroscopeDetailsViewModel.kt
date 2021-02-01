package com.wiuma.astraios.ui.horoscopeDetails

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wiuma.astraios.models.Horoscope
import com.wiuma.astraios.service.HoroscopeAPIService
import com.wiuma.astraios.service.HoroscopeDatabase
import com.wiuma.astraios.ui.BaseViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class HoroscopeDetailsViewModel(application: Application) : BaseViewModel(application) {
    val horoscopeDetails = MutableLiveData<Horoscope>()

    fun getData(horoscopeId: Int) {
        launch {
            val dao = HoroscopeDatabase(getApplication()).horoscopeDao()
            val horoscope = dao.getHoroscope(horoscopeId)
            horoscopeDetails.value = horoscope
        }
    }

}
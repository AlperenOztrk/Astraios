package com.wiuma.astraios.ui.horoscope

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.wiuma.astraios.models.Horoscope
import com.wiuma.astraios.service.HoroscopeAPIService
import com.wiuma.astraios.service.HoroscopeDatabase
import com.wiuma.astraios.ui.BaseViewModel
import com.wiuma.astraios.utilities.PrivateSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class HoroscopeViewModel(application: Application) : BaseViewModel(application) {
    private var updateFrequency = 30 * 60 * 1000 * 1000 * 1000L //For 30 minutes of nano time.
    private val horoscopeApiService = HoroscopeAPIService()
    private val disposable = CompositeDisposable()
    private val privateSharedPreferences = PrivateSharedPreferences(getApplication())
    val horoscopeList = MutableLiveData<List<Horoscope>>()
    val successBoolean = MutableLiveData<Boolean>()
    val loadingStatus = MutableLiveData<Boolean>()

    fun fetchData() {
        val lastTime = privateSharedPreferences.getTime()
        if (lastTime != null && lastTime != 0L && System.nanoTime() - updateFrequency < lastTime) {
            getDataFromData()
        } else {
            getDataFromAPI()
        }
    }

    private fun getDataFromAPI() {
        loadingStatus.value = true
        disposable.add(
            horoscopeApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Horoscope>>() {
                    override fun onSuccess(t: List<Horoscope>) {
                        saveData(t)
                    }

                    override fun onError(e: Throwable) {
                        loadingStatus.value = false
                        successBoolean.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun getDataFromData() {
        launch {
            val horoscopeList = HoroscopeDatabase(getApplication()).horoscopeDao().getAll()
            showData(horoscopeList)
        }
    }

    private fun saveData(horoscopeList: List<Horoscope>) {
        launch {
            val dao = HoroscopeDatabase(getApplication()).horoscopeDao()
            dao.deleteAll()
            var idList = dao.insertAll(*horoscopeList.toTypedArray())
            var i = 0
            while (i < horoscopeList.size) {
                horoscopeList[i].Id = idList[i].toInt()
                i = i + 1
            }
            showData(horoscopeList)
        }
        privateSharedPreferences.saveTime(System.nanoTime())
    }

    private fun showData(list: List<Horoscope>) {
        horoscopeList.value = list
        successBoolean.value = true
        loadingStatus.value = false
    }

}
package com.wiuma.astraios.ui.horoscope

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wiuma.astraios.models.Horoscope
import com.wiuma.astraios.service.HoroscopeAPIService
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class HoroscopeViewModel : ViewModel() {
    private val horoscopeApiService = HoroscopeAPIService()
    private val disposable = CompositeDisposable()
    val horoscopeList = MutableLiveData<List<Horoscope>>()
    val successBoolean = MutableLiveData<Boolean>()
    val loadingStatus = MutableLiveData<Boolean>()

    fun getDataFromAPI() {
        loadingStatus.value = true
        disposable.add(
            horoscopeApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Horoscope>>() {
                    override fun onSuccess(t: List<Horoscope>) {
                        horoscopeList.value = t
                        successBoolean.value = true
                        loadingStatus.value = false
                    }

                    override fun onError(e: Throwable) {
                        loadingStatus.value = false
                        successBoolean.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

}
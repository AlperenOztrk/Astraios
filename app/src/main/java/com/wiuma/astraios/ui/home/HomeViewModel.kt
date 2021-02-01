package com.wiuma.astraios.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.wiuma.astraios.models.Horoscope
import com.wiuma.astraios.models.User
import com.wiuma.astraios.service.HoroscopeAPIService
import com.wiuma.astraios.service.HoroscopeDatabase
import com.wiuma.astraios.service.UserDatabase
import com.wiuma.astraios.ui.BaseViewModel
import com.wiuma.astraios.utilities.PrivateSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(application: Application) : BaseViewModel(application) {
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser?.uid
    private var updateFrequency = 30 * 60 * 1000 * 1000 * 1000L //For 30 minutes of nano time.
    private val horoscopeApiService = HoroscopeAPIService()
    private val disposable = CompositeDisposable()
    val horoscope = MutableLiveData<Horoscope>()
    private val privateSharedPreferences = PrivateSharedPreferences(getApplication())
    val successBoolean = MutableLiveData<Boolean>()
    val loadingStatus = MutableLiveData<Boolean>()
    val date = MutableLiveData<String>()
    val user = MutableLiveData<User>()


    fun fetchData() {
        val sdf = SimpleDateFormat.getDateInstance()
        val currentDate = sdf.format(Date())
        date.value = currentDate
        getUserData()
    }

    private fun getUserData() {
        launch {
            val dataUser = UserDatabase(getApplication()).userDao().getUser(currentUser)
            user.value = dataUser
            val lastTime = privateSharedPreferences.getTime()
            if (lastTime != null && lastTime != 0L && System.nanoTime() - updateFrequency < lastTime) {
                dataUser.horoscope?.let { getDataFromData(it.toInt()) }
            } else {
                dataUser.horoscope?.let { getDataFromAPI(it.toInt()) }
            }
        }
    }

    private fun getDataFromAPI(id: Int) {
        loadingStatus.value = true
        disposable.add(
            horoscopeApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Horoscope>>() {
                    override fun onSuccess(t: List<Horoscope>) {
                        saveData(t)
                        for (i in t) {
                            if (i.Id == id) {
                                val usersHoroscope = Horoscope(
                                    i.Id,
                                    i.nameHoroscope,
                                    i.signHoroscope,
                                    i.shadowSignHoroscope,
                                    i.dailyComment,
                                    i.startDate,
                                    i.endDate,
                                    i.namePlanet,
                                    i.signPlanet,
                                    i.nameElement,
                                    i.signElement,
                                    i.highlightOfHoroscope,
                                    i.characterOfHoroscope,
                                    i.InkSign
                                )
                                horoscope.value = usersHoroscope
                            }
                        }
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

    private fun saveData(horoscopeList: List<Horoscope>) {
        launch {
            val dao = HoroscopeDatabase(getApplication()).horoscopeDao()
            dao.deleteAll()
            val idList = dao.insertAll(*horoscopeList.toTypedArray())
            var i = 0
            while (i < horoscopeList.size) {
                horoscopeList[i].Id = idList[i].toInt()
                i = i + 1
            }
        }
        privateSharedPreferences.saveTime(System.nanoTime())
    }

    private fun getDataFromData(id: Int) {
        launch {
            val horoscopeList = HoroscopeDatabase(getApplication()).horoscopeDao().getAll()
            for (i in horoscopeList) {
                if (i.Id == id) {
                    val usersHoroscope = Horoscope(
                        i.Id,
                        i.nameHoroscope,
                        i.signHoroscope,
                        i.shadowSignHoroscope,
                        i.dailyComment,
                        i.startDate,
                        i.endDate,
                        i.namePlanet,
                        i.signPlanet,
                        i.nameElement,
                        i.signElement,
                        i.highlightOfHoroscope,
                        i.characterOfHoroscope,
                        i.InkSign
                    )
                    horoscope.value = usersHoroscope
                }
            }
            successBoolean.value = true
            loadingStatus.value = false
        }
    }

}
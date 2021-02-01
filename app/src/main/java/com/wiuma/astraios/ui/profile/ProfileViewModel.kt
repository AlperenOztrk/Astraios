package com.wiuma.astraios.ui.profile

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.wiuma.astraios.models.Horoscope
import com.wiuma.astraios.models.User
import com.wiuma.astraios.service.HoroscopeDatabase
import com.wiuma.astraios.service.UserDatabase
import com.wiuma.astraios.ui.BaseViewModel
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : BaseViewModel(application) {
    val user = MutableLiveData<User>()
    val updated = MutableLiveData<Boolean>()
    val horoscope = MutableLiveData<Horoscope>()
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser?.uid

    fun getDataFromData() {
        launch {
            val dataUser = UserDatabase(getApplication()).userDao().getUser(currentUser)
            user.value = dataUser
            dataUser.horoscope?.let { getHoroscopeInfo(it) }
        }
    }

    fun getHoroscopeInfo(id: String) {
        launch {
            val horoscopeList = HoroscopeDatabase(getApplication()).horoscopeDao().getAll()
            for (i in horoscopeList) {
                if (i.Id == id.toInt()) {
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
        }
    }

    fun updateUser(
        updatePassword: Boolean,
        updateEmail: Boolean,
        newPass: String?,
        name: String,
        newEmail: String,
        newBirthday: String,
        newHoroscope: String,
        newAge: String
    ) {
        launch {
            if (newPass != null) {
                if (updatePassword) {
                    val user = auth.currentUser
                    user!!.updatePassword(newPass)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                            }
                        }
                }
            }
            if (updateEmail) {
                val user = auth.currentUser
                user!!.updateEmail(newEmail)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                        }
                    }
            }
            val newUser =
                User(auth.currentUser!!.uid, name, newBirthday, newHoroscope, newAge, newEmail)
            UserDatabase(getApplication()).userDao().updateUser(newUser)
            Toast.makeText(getApplication(), "User Updated", Toast.LENGTH_LONG).show()
            updated.value = true
        }
    }
}
package com.wiuma.astraios

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.wiuma.astraios.models.Horoscope
import com.wiuma.astraios.models.User
import com.wiuma.astraios.service.HoroscopeAPIService
import com.wiuma.astraios.service.UserDatabase
import com.wiuma.astraios.utilities.createPlaceholder
import com.wiuma.astraios.utilities.fetchImage
import com.wiuma.astraios.utilities.findAge
import com.wiuma.astraios.utilities.findHoroscope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class UserProfileActivity : AppCompatActivity(), CoroutineScope {
    private var succesfullySignedIn = false
    private lateinit var auth: FirebaseAuth
    private val horoscopeApiService = HoroscopeAPIService()
    private val disposable = CompositeDisposable()
    private val job = Job()
    private var birthdayGlobal: String = ""
    private var nameGlobal: String = ""
    private lateinit var ageGlobal: String
    private lateinit var horoscopeGlobal: String
    private lateinit var birthdayButton: Button
    private lateinit var saveButton: Button
    private lateinit var ageTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var horoscopeNameTextView: TextView
    private lateinit var horoscopeDatesTextView: TextView
    private lateinit var horoscopeSignImageView: ImageView
    private lateinit var horoscopeCardView: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        auth = FirebaseAuth.getInstance()
        ageTextView = findViewById(R.id.age)
        nameTextView = findViewById(R.id.name)
        birthdayButton = findViewById(R.id.birthday)
        saveButton = findViewById(R.id.saveButton)
        horoscopeNameTextView = findViewById(R.id.horoscopeName)
        horoscopeDatesTextView = findViewById(R.id.horoscopeDates)
        horoscopeSignImageView = findViewById(R.id.horoscopeSign)
        horoscopeCardView = findViewById(R.id.horoscopeCardView)
    }

    fun View.saveUser() {
        var nameCheck = true
        var birthdayCheck = true
        nameGlobal = nameTextView.text.toString()
        if (nameGlobal == "") {
            nameCheck = false
            Snackbar.make(
                this@UserProfileActivity.findViewById(android.R.id.content),
                "Lütfen İsim giriniz.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        if (birthdayGlobal == "") {
            birthdayCheck = false
            Snackbar.make(
                this@UserProfileActivity.findViewById(android.R.id.content),
                "Lütfen doğum tarihinizi seçiniz.",
                Snackbar.LENGTH_SHORT
            ).show()
        }
        if (nameCheck || birthdayCheck) {
            initializeRecords()
        }
    }

    fun View.datePicker() {
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd.MM.yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                birthdayGlobal = sdf.format(cal.time)
                birthdayButton.text = sdf.format(cal.time)
                ageGlobal = findAge(year, monthOfYear, dayOfMonth)
                ageTextView.text = ageGlobal
                horoscopeGlobal = findHoroscope(monthOfYear, dayOfMonth).toString()
                getHoroscope(findHoroscope(monthOfYear, dayOfMonth))
            }

        DatePickerDialog(
            this@UserProfileActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun getHoroscope(horoscope: Int) {
        disposable.add(
            horoscopeApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Horoscope>>() {
                    @SuppressLint("SetTextI18n")
                    override fun onSuccess(t: List<Horoscope>) {
                        for (i in t) {
                            if (i.Id == horoscope) {
                                horoscopeNameTextView.text = i.nameHoroscope
                                horoscopeDatesTextView.text = i.startDate + " - " + i.endDate
                                horoscopeSignImageView.fetchImage(
                                    i.signHoroscope,
                                    createPlaceholder(applicationContext)
                                )
                            }
                        }
                        horoscopeCardView.visibility = View.VISIBLE
                        saveButton.visibility = View.VISIBLE
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun initializeRecords() {
        var initialize: Long = 0
        launch {
            val loggedUser =
                User(
                    auth.currentUser!!.uid,
                    nameGlobal,
                    birthdayGlobal,
                    horoscopeGlobal,
                    ageGlobal,
                    auth.currentUser!!.email
                )
            val dao = UserDatabase(application).userDao()
            dao.deleteAll()
            initialize = dao.insert(loggedUser)
            println(initialize)
            if (initialize > 0) {
                succesfullySignedIn = true
                seeEnded()
            }
        }
    }

    fun seeEnded() {
        if (succesfullySignedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onDestroy() {
        super.onDestroy()
        if (!succesfullySignedIn) {
            auth.signOut()
        }
    }

}
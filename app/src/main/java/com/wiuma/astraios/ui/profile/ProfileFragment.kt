package com.wiuma.astraios.ui.profile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.wiuma.astraios.LoginActivity
import com.wiuma.astraios.R
import com.wiuma.astraios.utilities.createPlaceholder
import com.wiuma.astraios.utilities.fetchImage
import com.wiuma.astraios.utilities.findAge
import com.wiuma.astraios.utilities.findHoroscope
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var nameTextView: TextView
    private lateinit var mailTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var birthdayButton: Button
    private lateinit var ageTextView: TextView
    private lateinit var horoscopeNameTextView: TextView
    private lateinit var horoscopeDatesTextView: TextView
    private lateinit var horoscopeSignImageView: ImageView
    private lateinit var auth: FirebaseAuth
    private lateinit var exitButton: Button
    private lateinit var updateButton: Button
    private var updatePassword = false
    private var updateEmail = false
    private lateinit var userAge: String
    private lateinit var userHoroscope: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileViewModel.getDataFromData()
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        nameTextView = root.findViewById(R.id.name)
        mailTextView = root.findViewById(R.id.emailAdress)
        passwordTextView = root.findViewById(R.id.password)
        birthdayButton = root.findViewById(R.id.birtday)
        horoscopeNameTextView = root.findViewById(R.id.horoscopeName)
        horoscopeDatesTextView = root.findViewById(R.id.horoscopeDates)
        horoscopeSignImageView = root.findViewById(R.id.horoscopeSign)
        ageTextView = root.findViewById(R.id.age)
        exitButton = root.findViewById(R.id.exitButton)
        updateButton = root.findViewById(R.id.updateButton)
        auth = FirebaseAuth.getInstance()
        observeData()
        return root
    }

    @SuppressLint("SetTextI18n", "ShowToast")
    fun observeData() {
        profileViewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                nameTextView.text = user.name
                mailTextView.text = user.email
                birthdayButton.text = user.birthday
                ageTextView.text = user.age
                userAge = user.age.toString()
                userHoroscope = user.horoscope.toString()
            }
        })
        profileViewModel.updated.observe(viewLifecycleOwner, Observer {
            passwordTextView.text = ""
        })

        profileViewModel.horoscope.observe(viewLifecycleOwner, Observer { horoscope ->
            horoscope?.let {
                horoscopeNameTextView.text = it.nameHoroscope
                horoscopeDatesTextView.text = it.startDate + " - " + it.endDate
                context?.let {
                    horoscopeSignImageView.fetchImage(
                        (horoscope.signHoroscope),
                        createPlaceholder(it)
                    )
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitButton.setOnClickListener {
            auth.signOut()
            activity?.let {
                val intent = Intent(it, LoginActivity::class.java)
                it.startActivity(intent)
                it.finish()
            }
        }
        birthdayButton.setOnClickListener {
            datePicker()
        }
        mailTextView.addTextChangedListener() {
            updateEmail = true
        }
        passwordTextView.addTextChangedListener() {
            updatePassword = true
        }
        updateButton.setOnClickListener {
            if (passwordTextView.text.isEmpty()) {
                updatePassword = false
            }
            profileViewModel.updateUser(
                updatePassword,
                updateEmail,
                passwordTextView.text.toString(),
                nameTextView.text.toString(),
                mailTextView.text.toString(),
                birthdayButton.text.toString(),
                userHoroscope,
                userAge
            )
        }
    }

    private fun datePicker() {
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd.MM.yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                birthdayButton.text = sdf.format(cal.time)
                userAge = findAge(year, monthOfYear, dayOfMonth)
                ageTextView.text = userAge
                userHoroscope = findHoroscope(monthOfYear, dayOfMonth).toString()
                profileViewModel.getHoroscopeInfo(userHoroscope)
            }

        context?.let {
            DatePickerDialog(
                it, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
}
package com.wiuma.astraios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailTextView: TextView
    private lateinit var passwordTextView: TextView
    private lateinit var loginCardView: CardView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        emailTextView = findViewById(R.id.emailAdress)
        passwordTextView = findViewById(R.id.password)
        loginCardView = findViewById(R.id.loginCardView)
        progressBar = findViewById(R.id.progressBar)

    }

    fun View.register() {
        loginCardView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        val email = emailTextView.text.toString()
        val password = passwordTextView.text.toString()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                succesfullyCompleted()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            loginCardView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    fun View.login() {
        loginCardView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        val email = emailTextView.text.toString()
        val password = passwordTextView.text.toString()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                succesfullyCompleted()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            loginCardView.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }

    private fun succesfullyCompleted() {
        val intent = Intent(this, UserProfileActivity::class.java)
        startActivity(intent)
        finish()
    }
}
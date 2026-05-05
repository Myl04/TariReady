package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class SigninActivity: AppCompatActivity () {
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var signinBtnSignin: Button
    private lateinit var registerBtnSignin: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        signinBtnSignin = findViewById(R.id.signinBtnSignin)
        registerBtnSignin = findViewById(R.id.registerBtnSignin)
        sharedPreferences = getSharedPreferences("TariReadyPrefs", Context.MODE_PRIVATE)

        signinBtnSignin.setOnClickListener {
            performLogin()
        }

        registerBtnSignin.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()

        val registeredUsername = sharedPreferences.getString("username", "")
        val registeredPassword = sharedPreferences.getString("password", "")
        val fullName = sharedPreferences.getString("fullName", "")
        val farmName = sharedPreferences.getString("farmName", "")

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        } else if (username == registeredUsername && password == registeredPassword) {
            val intent = Intent(this, ProfileActivity::class.java).apply {
                putExtra("fullName", fullName)
                putExtra("username", username)
                putExtra("farmName", farmName)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
        }
    }
}
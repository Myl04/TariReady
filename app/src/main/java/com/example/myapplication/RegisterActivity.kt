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

class RegisterActivity:AppCompatActivity() {
    private lateinit var arFullname: EditText
    private lateinit var arUsername: EditText
    private lateinit var arFarmname: EditText
    private lateinit var arPassword: EditText
    private lateinit var arConfirmation: EditText
    private lateinit var registerBtnRegister: Button
    private lateinit var signinBtnRegister: TextView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        arFullname = findViewById(R.id.arFullname)
        arUsername = findViewById(R.id.arUsername)
        arFarmname = findViewById(R.id.arFarmname)
        arPassword = findViewById(R.id.arPassword)
        arConfirmation = findViewById(R.id.arConfirmation)
        registerBtnRegister = findViewById(R.id.registerBtnRegister)
        signinBtnRegister = findViewById(R.id.signinBtnRegister)

        sharedPreferences = getSharedPreferences("TariReadyPrefs", Context.MODE_PRIVATE)

        registerBtnRegister.setOnClickListener {
            performRegistration()
        }

        signinBtnRegister.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }

    private fun performRegistration() {
        val fullName = arFullname.text.toString().trim()
        val username = arUsername.text.toString().trim()
        val farmName = arFarmname.text.toString().trim()
        val password = arPassword.text.toString().trim()
        val confirmPassword = arConfirmation.text.toString().trim()

        if (fullName.isEmpty()) {
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show()
            return
        }
        if (username.isEmpty()) {
            Toast.makeText(this, "Please choose a username", Toast.LENGTH_SHORT).show()
            return
        }
        if (farmName.isEmpty()) {
            Toast.makeText(this, "Please enter your farm name", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Please create a password", Toast.LENGTH_SHORT).show()
            return
        }
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        val editor = sharedPreferences.edit()
        editor.putString("fullName", fullName)
        editor.putString("username", username)
        editor.putString("farmName", farmName)
        editor.putString("password", password)
        editor.apply()

        Toast.makeText(this, "Registration Successful! Please Sign In.", Toast.LENGTH_LONG).show()

        val intent = Intent(this, SigninActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
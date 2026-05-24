package com.example.myapplication.auth.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.TariReadyApplication
import com.example.myapplication.auth.contract.AuthContract
import com.example.myapplication.auth.presenter.RegisterPresenter
import com.example.myapplication.extensions.showToast

class RegisterActivity : AppCompatActivity(), AuthContract.RegisterView {

    private lateinit var arFullname: EditText
    private lateinit var arUsername: EditText
    private lateinit var arFarmname: EditText
    private lateinit var arPassword: EditText
    private lateinit var arConfirmation: EditText
    private lateinit var registerBtnRegister: Button
    private lateinit var signinBtnRegister: TextView

    private lateinit var presenter: AuthContract.RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // ✅ Same prefs instance as SigninActivity — data flows correctly
        val prefs = (application as TariReadyApplication).sharedPreferences
        presenter = RegisterPresenter(this, prefs)

        arFullname         = findViewById(R.id.arFullname)
        arUsername         = findViewById(R.id.arUsername)
        arFarmname         = findViewById(R.id.arFarmname)
        arPassword         = findViewById(R.id.arPassword)
        arConfirmation     = findViewById(R.id.arConfirmation)
        registerBtnRegister = findViewById(R.id.registerBtnRegister)
        signinBtnRegister  = findViewById(R.id.signinBtnRegister)

        registerBtnRegister.setOnClickListener {
            presenter.onRegisterClicked(
                arFullname.text.toString().trim(),
                arUsername.text.toString().trim(),
                arFarmname.text.toString().trim(),
                arPassword.text.toString().trim(),
                arConfirmation.text.toString().trim()
            )
        }

        signinBtnRegister.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            finish()
        }
    }

    override fun showError(message: String) = showToast(message)

    override fun onRegistrationSuccess() {
        showToast("Registration Successful! Please Sign In.", Toast.LENGTH_LONG)
        val intent = Intent(this, SigninActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
}
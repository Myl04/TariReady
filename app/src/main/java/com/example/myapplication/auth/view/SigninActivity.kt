package com.example.myapplication.auth.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.TariReadyApplication
import com.example.myapplication.auth.contract.AuthContract
import com.example.myapplication.auth.presenter.SigninPresenter
import com.example.myapplication.dashboard.view.DashboardActivity
import com.example.myapplication.extensions.showToast

class SigninActivity : AppCompatActivity(), AuthContract.SigninView {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var signinBtnSignin: Button
    private lateinit var registerBtnSignin: TextView

    private lateinit var presenter: AuthContract.SigninPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val prefs = (application as TariReadyApplication).sharedPreferences
        presenter = SigninPresenter(this, prefs)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        signinBtnSignin = findViewById(R.id.signinBtnSignin)
        registerBtnSignin = findViewById(R.id.registerBtnSignin)

        signinBtnSignin.setOnClickListener {
            presenter.onSignInClicked(
                etUsername.text.toString().trim(),
                etPassword.text.toString().trim()
            )
        }

        registerBtnSignin.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun showError(message: String) = showToast(message)

    override fun navigateToProfile(fullName: String, username: String, farmName: String) {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
        showToast("Welcome back!")
    }
}

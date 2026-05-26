package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.auth.view.RegisterActivity
import com.example.myapplication.auth.view.SigninActivity
import com.example.myapplication.dashboard.view.DashboardActivity
import com.example.myapplication.extensions.isLoggedIn

class MainActivity : AppCompatActivity() {

    private lateinit var signinBtnMain: Button
    private lateinit var registerBtnMain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = (application as TariReadyApplication).sharedPreferences
        if (prefs.isLoggedIn()) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        signinBtnMain  = findViewById(R.id.signinBtnMain)
        registerBtnMain = findViewById(R.id.registerBtnMain)

        signinBtnMain.setOnClickListener {
            startActivity(Intent(this, SigninActivity::class.java))
        }

        registerBtnMain.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}

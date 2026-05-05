package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.auth.view.RegisterActivity
import com.example.myapplication.auth.view.SigninActivity

class MainActivity : AppCompatActivity() {

    private lateinit var signinBtnMain: Button
    private lateinit var registerBtnMain: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signinBtnMain = findViewById(R.id.signinBtnMain)
        registerBtnMain = findViewById(R.id.registerBtnMain)

        signinBtnMain.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        registerBtnMain.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
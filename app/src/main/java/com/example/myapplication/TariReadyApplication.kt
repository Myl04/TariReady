package com.example.myapplication

import android.app.Application
import android.content.SharedPreferences

class TariReadyApplication : Application() {

    lateinit var sharedPreferences: SharedPreferences
        private set

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("TariReadyPrefs", MODE_PRIVATE)
    }
}
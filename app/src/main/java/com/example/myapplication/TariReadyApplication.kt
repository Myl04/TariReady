package com.example.myapplication

import android.app.Application
import android.content.SharedPreferences
import com.example.myapplication.data.repository.SupplyRepository

class TariReadyApplication : Application() {

    lateinit var sharedPreferences: SharedPreferences
        private set

    lateinit var supplyRepository: SupplyRepository
        private set

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("TariReadyPrefs", MODE_PRIVATE)
        supplyRepository = SupplyRepository(sharedPreferences)
    }
}
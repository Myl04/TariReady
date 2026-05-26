package com.example.myapplication.extensions

import android.content.SharedPreferences

data class UserData(
    val fullName: String,
    val username: String,
    val farmName: String,
    val password: String
)

fun SharedPreferences.saveUser(fullName: String, username: String, farmName: String, password: String) {
    edit().apply {
        putString("fullName", fullName)
        putString("username", username)
        putString("farmName", farmName)
        putString("password", password)
        putBoolean("isLoggedIn", true)   // mark session as active
        apply()
    }
}

fun SharedPreferences.getUser(): UserData {
    return UserData(
        fullName = getString("fullName", "") ?: "",
        username = getString("username", "") ?: "",
        farmName = getString("farmName", "") ?: "",
        password = getString("password", "") ?: ""
    )
}

fun SharedPreferences.clearUser() {
    edit().putBoolean("isLoggedIn", false).apply()
}

fun SharedPreferences.isLoggedIn(): Boolean {
    return getBoolean("isLoggedIn", false)
}

package com.example.myapplication.auth.presenter

import android.content.SharedPreferences
import com.example.myapplication.auth.contract.AuthContract
import com.example.myapplication.extensions.saveUser

class RegisterPresenter(
    private val view: AuthContract.RegisterView,
    private val sharedPreferences: SharedPreferences
) : AuthContract.RegisterPresenter {

    override fun onRegisterClicked(
        fullName: String,
        username: String,
        farmName: String,
        password: String,
        confirmPassword: String
    ) {
        if (fullName.isEmpty()) {
            view.showError("Please enter your full name")
            return
        }
        if (username.isEmpty()) {
            view.showError("Please choose a username")
            return
        }
        if (farmName.isEmpty()) {
            view.showError("Please enter your farm name")
            return
        }
        if (password.isEmpty()) {
            view.showError("Please create a password")
            return
        }
        if (password != confirmPassword) {
            view.showError("Passwords do not match")
            return
        }
        if (password.length < 6) {
            view.showError("Password must be at least 6 characters")
            return
        }

        sharedPreferences.saveUser(fullName, username, farmName, password)
        view.onRegistrationSuccess()
    }
}
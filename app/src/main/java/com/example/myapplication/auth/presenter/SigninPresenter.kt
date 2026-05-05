package com.example.myapplication.auth.presenter

import android.content.SharedPreferences
import com.example.myapplication.auth.contract.AuthContract
import com.example.myapplication.extensions.getUser

class SigninPresenter(
    private val view: AuthContract.SigninView,
    private val sharedPreferences: SharedPreferences
) : AuthContract.SigninPresenter {

    override fun onSignInClicked(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError("Please fill in all fields")
            return
        }

        val user = sharedPreferences.getUser()

        if (username == user.username && password == user.password) {
            view.navigateToProfile(user.fullName, username, user.farmName)
        } else {
            view.showError("Invalid username or password")
        }
    }
}
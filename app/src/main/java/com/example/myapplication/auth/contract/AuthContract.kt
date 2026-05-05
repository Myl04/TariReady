package com.example.myapplication.auth.contract

interface AuthContract {

    interface SigninView {
        fun showError(message: String)
        fun navigateToProfile(fullName: String, username: String, farmName: String)
    }

    interface SigninPresenter {
        fun onSignInClicked(username: String, password: String)
    }

    interface RegisterView {
        fun showError(message: String)
        fun onRegistrationSuccess()
    }

    interface RegisterPresenter {
        fun onRegisterClicked(
            fullName: String,
            username: String,
            farmName: String,
            password: String,
            confirmPassword: String
        )
    }
}
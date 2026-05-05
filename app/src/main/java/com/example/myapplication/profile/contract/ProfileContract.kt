package com.example.myapplication.profile.contract

interface ProfileContract {

    interface ProfileView {
        fun displayUserData(fullName: String, username: String, farmName: String)
        fun showMessage(message: String)
        fun showSettingsDialog()
        fun showLogoutConfirmation()
        fun navigateToMain()
    }

    interface ProfilePresenter {
        fun loadUserData(
            intentFullName: String?,
            intentUsername: String?,
            intentFarmName: String?
        )
        fun onSettingsClicked()
        fun onLogoutClicked()
        fun onLogoutConfirmed()
        fun onHomeClicked()
        fun onInventoryClicked()
        fun onHistoryClicked()
        fun onProfileClicked()
    }
}
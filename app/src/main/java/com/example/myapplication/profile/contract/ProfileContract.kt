package com.example.myapplication.profile.contract

interface ProfileContract {

    interface ProfileView {
        fun displayUserData(fullName: String, username: String, farmName: String)
        fun showMessage(message: String)
        fun showSettingsDialog()
        fun showLogoutConfirmation()
        fun navigateToMain()
        fun navigateToDashboard()
        fun navigateToInventory()
        fun navigateToHistory()
    }

    interface ProfilePresenter {
        fun loadUserData()
        fun onSettingsClicked()
        fun onLogoutClicked()
        fun onLogoutConfirmed()
        fun onDashboardClicked()
        fun onInventoryClicked()
        fun onHistoryClicked()
        fun onProfileClicked()
    }
}

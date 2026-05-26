package com.example.myapplication.profile.presenter

import android.content.SharedPreferences
import com.example.myapplication.extensions.clearUser
import com.example.myapplication.extensions.getUser
import com.example.myapplication.profile.contract.ProfileContract

class ProfilePresenter(
    private val view: ProfileContract.ProfileView,
    private val prefs: SharedPreferences
) : ProfileContract.ProfilePresenter {

    override fun loadUserData() {
        val user = prefs.getUser()
        view.displayUserData(
            fullName = user.fullName.ifEmpty { "User" },
            username = user.username.ifEmpty { "username" },
            farmName = user.farmName.ifEmpty { "Farm Name" }
        )
    }

    override fun onSettingsClicked() = view.showSettingsDialog()

    override fun onLogoutClicked() = view.showLogoutConfirmation()

    override fun onLogoutConfirmed() {
        view.showMessage("Logged out successfully")
        view.navigateToMain()
    }

    override fun onDashboardClicked() = view.navigateToDashboard()
    override fun onInventoryClicked() = view.navigateToInventory()
    override fun onHistoryClicked() = view.navigateToHistory()
    override fun onProfileClicked() = view.showMessage("Profile — you are here")
}

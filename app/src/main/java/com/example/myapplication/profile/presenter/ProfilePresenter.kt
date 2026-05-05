package com.example.myapplication.profile.presenter

import android.content.SharedPreferences
import com.example.myapplication.extensions.clearUser
import com.example.myapplication.extensions.getUser
import com.example.myapplication.profile.contract.ProfileContract

class ProfilePresenter(
    private val view: ProfileContract.ProfileView,
    private val sharedPreferences: SharedPreferences
) : ProfileContract.ProfilePresenter {

    override fun loadUserData(
        intentFullName: String?,
        intentUsername: String?,
        intentFarmName: String?
    ) {
        val saved = sharedPreferences.getUser()
        val fullName = intentFullName?.takeIf { it.isNotEmpty() } ?: saved.fullName.ifEmpty { "User Name" }
        val username = intentUsername?.takeIf { it.isNotEmpty() } ?: saved.username.ifEmpty { "username" }
        val farmName = intentFarmName?.takeIf { it.isNotEmpty() } ?: saved.farmName.ifEmpty { "Farm Name" }
        view.displayUserData(fullName, username, farmName)
    }

    override fun onSettingsClicked() {
        view.showSettingsDialog()
    }

    override fun onLogoutClicked() {
        view.showLogoutConfirmation()
    }

    override fun onLogoutConfirmed() {
        sharedPreferences.clearUser()
        view.navigateToMain()
        view.showMessage("Logged out successfully")
    }

    override fun onHomeClicked() {
        view.showMessage("Home - Dashboard")
    }

    override fun onInventoryClicked() {
        view.showMessage("Inventory - Manage Supplies")
    }

    override fun onHistoryClicked() {
        view.showMessage("History - View Past Activities")
    }

    override fun onProfileClicked() {
        view.showMessage("Profile - You are here")
    }
}
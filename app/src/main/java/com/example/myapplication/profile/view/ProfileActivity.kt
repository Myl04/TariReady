package com.example.myapplication.profile.view

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.TariReadyApplication
import com.example.myapplication.extensions.showToast
import com.example.myapplication.profile.contract.ProfileContract
import com.example.myapplication.profile.presenter.ProfilePresenter

class ProfileActivity : AppCompatActivity(), ProfileContract.ProfileView {

    private lateinit var hamburgerButton: ImageButton
    private lateinit var pfpFullname: TextView
    private lateinit var pfpUsername2: TextView
    private lateinit var pfpFarmName: TextView
    private lateinit var homeTextView: TextView
    private lateinit var inventoryTextView: TextView
    private lateinit var historyTextView: TextView
    private lateinit var profileTextView: TextView

    private lateinit var presenter: ProfileContract.ProfilePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val prefs = (application as TariReadyApplication).sharedPreferences
        presenter = ProfilePresenter(this, prefs)

        pfpFullname = findViewById(R.id.pfpFullname)
        pfpUsername2 = findViewById(R.id.pfpUsername2)
        pfpFarmName = findViewById(R.id.pfpFarmName)
        homeTextView = findViewById(R.id.homeTextView)
        inventoryTextView = findViewById(R.id.inventoryTextView)
        historyTextView = findViewById(R.id.historyTextView)
        profileTextView = findViewById(R.id.profileTextView)
        hamburgerButton = findViewById(R.id.hamburgerButton)

        presenter.loadUserData(
            intent.getStringExtra("fullName"),
            intent.getStringExtra("username"),
            intent.getStringExtra("farmName")
        )

        hamburgerButton.setOnClickListener { view -> showPopupMenu(view) }

        homeTextView.setOnClickListener { presenter.onHomeClicked() }
        inventoryTextView.setOnClickListener { presenter.onInventoryClicked() }
        historyTextView.setOnClickListener { presenter.onHistoryClicked() }
        profileTextView.setOnClickListener { presenter.onProfileClicked() }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.gravity = Gravity.END
        popupMenu.menu.add(0, R.id.settingsOption, 0, "Settings")
        popupMenu.menu.add(0, R.id.logoutOption, 1, "Logout")
        popupMenu.menu.findItem(R.id.settingsOption).setIcon(R.drawable.settings_icon)
        popupMenu.menu.findItem(R.id.logoutOption).setIcon(R.drawable.signout_icon)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settingsOption -> { presenter.onSettingsClicked(); true }
                R.id.logoutOption -> { presenter.onLogoutClicked(); true }
                else -> false
            }
        }
        popupMenu.show()
    }

    override fun displayUserData(fullName: String, username: String, farmName: String) {
        pfpFullname.text = fullName
        pfpUsername2.text = username
        pfpFarmName.text = farmName
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    override fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Settings")
            .setMessage("Settings options will be available soon!\n\n• Notification Preferences\n• Language Settings\n• Privacy Settings\n• Account Management")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ -> presenter.onLogoutConfirmed() }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}
package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var hamburgerButton: ImageButton
    private lateinit var pfpFullname: TextView
    private lateinit var pfpUsername2: TextView
    private lateinit var pfpFarmName: TextView
    private lateinit var homeTextView: TextView
    private lateinit var inventoryTextView: TextView
    private lateinit var historyTextView: TextView
    private lateinit var profileTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences

    private var fullName: String = ""
    private var username: String = ""
    private var farmName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        pfpFullname = findViewById(R.id.pfpFullname)
        pfpUsername2 = findViewById(R.id.pfpUsername2)
        pfpFarmName = findViewById(R.id.pfpFarmName)
        homeTextView = findViewById(R.id.homeTextView)
        inventoryTextView = findViewById(R.id.inventoryTextView)
        historyTextView = findViewById(R.id.historyTextView)
        profileTextView = findViewById(R.id.profileTextView)
        hamburgerButton = findViewById(R.id.hamburgerButton)

        sharedPreferences = getSharedPreferences("TariReadyPrefs", Context.MODE_PRIVATE)

        fullName = intent.getStringExtra("fullName") ?: sharedPreferences.getString("fullName", "User Name").toString()
        username = intent.getStringExtra("username") ?: sharedPreferences.getString("username", "username").toString()
        farmName = intent.getStringExtra("farmName") ?: sharedPreferences.getString("farmName", "Farm Name").toString()

        pfpFullname.text = fullName
        pfpUsername2.text = username
        pfpFarmName.text = farmName

        setupPopupMenu()

        setupClickListeners()
    }

    private fun setupPopupMenu() {
        hamburgerButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
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
                R.id.settingsOption -> {
                    showSettingsDialog()
                    true
                }
                R.id.logoutOption -> {
                    showLogoutConfirmation()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }

    private fun setupClickListeners() {
        homeTextView.setOnClickListener {
            Toast.makeText(this, "Home - Dashboard", Toast.LENGTH_SHORT).show()
        }
        inventoryTextView.setOnClickListener {
            Toast.makeText(this, "Inventory - Manage Supplies", Toast.LENGTH_SHORT).show()
        }
        historyTextView.setOnClickListener {
            Toast.makeText(this, "History - View Past Activities", Toast.LENGTH_SHORT).show()
        }
        profileTextView.setOnClickListener {
            Toast.makeText(this, "Profile - You are here", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Settings")
            .setMessage("Settings options will be available soon!\n\n• Notification Preferences\n• Language Settings\n• Privacy Settings\n• Account Management")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                performLogout()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun performLogout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
    }

}
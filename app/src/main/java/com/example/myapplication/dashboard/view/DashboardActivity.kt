package com.example.myapplication.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.TariReadyApplication
import com.example.myapplication.data.model.HistoryEntry
import com.example.myapplication.data.model.Supply
import com.example.myapplication.dashboard.contract.DashboardContract
import com.example.myapplication.dashboard.presenter.DashboardPresenter
import com.example.myapplication.history.view.HistoryActivity
import com.example.myapplication.inventory.view.InventoryActivity
import com.example.myapplication.profile.view.ProfileActivity
import com.example.myapplication.supply.view.AddSupplyActivity

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var tvGreeting: TextView
    private lateinit var tvUsername: TextView
    private lateinit var tvTotalSupplies: TextView
    private lateinit var tvLowStock: TextView
    private lateinit var tvExpiring: TextView
    private lateinit var llLowStockAlerts: LinearLayout
    private lateinit var llRecentActivity: LinearLayout
    private lateinit var tvNoAlerts: TextView
    private lateinit var tvNoActivity: TextView
    private lateinit var navHome: TextView
    private lateinit var navInventory: TextView
    private lateinit var navHistory: TextView
    private lateinit var navProfile: TextView

    private lateinit var presenter: DashboardContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val app = application as TariReadyApplication
        presenter = DashboardPresenter(this, app.supplyRepository, app.sharedPreferences)

        bindViews()
        setupNavigation()
        setupGreeting()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadDashboard()
    }

    private fun bindViews() {
        tvGreeting       = findViewById(R.id.tvGreeting)
        tvUsername       = findViewById(R.id.tvDashboardUsername)
        tvTotalSupplies  = findViewById(R.id.tvTotalSupplies)
        tvLowStock       = findViewById(R.id.tvLowStockCount)
        tvExpiring       = findViewById(R.id.tvExpiringCount)
        llLowStockAlerts = findViewById(R.id.llLowStockAlerts)
        llRecentActivity = findViewById(R.id.llRecentActivity)
        tvNoAlerts       = findViewById(R.id.tvNoAlerts)
        tvNoActivity     = findViewById(R.id.tvNoActivity)
        navHome          = findViewById(R.id.navHome)
        navInventory     = findViewById(R.id.navInventory)
        navHistory       = findViewById(R.id.navHistory)
        navProfile       = findViewById(R.id.navProfile)
    }

    private fun setupNavigation() {
        navHome.setOnClickListener { /* already here */ }
        navInventory.setOnClickListener { presenter.onInventoryClicked() }
        navHistory.setOnClickListener { presenter.onHistoryClicked() }
        navProfile.setOnClickListener { presenter.onProfileClicked() }
    }

    private fun setupGreeting() {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        tvGreeting.text = when {
            hour < 12 -> "Good morning,"
            hour < 17 -> "Good afternoon,"
            else      -> "Good evening,"
        }
    }

    // ─── DashboardContract.View ───────────────────────────────────────────────

    override fun showUsername(name: String) {
        tvUsername.text = name
    }

    override fun showStats(total: Int, lowStock: Int, expiring: Int) {
        tvTotalSupplies.text = total.toString()
        tvLowStock.text      = lowStock.toString()
        tvExpiring.text      = expiring.toString()
    }

    override fun showLowStockAlerts(items: List<Supply>) {
        llLowStockAlerts.removeAllViews()
        if (items.isEmpty()) {
            tvNoAlerts.visibility = View.VISIBLE
            return
        }
        tvNoAlerts.visibility = View.GONE
        items.take(3).forEach { supply ->
            val row = LayoutInflater.from(this)
                .inflate(R.layout.item_low_stock_alert, llLowStockAlerts, false)
            row.findViewById<TextView>(R.id.tvAlertName).text = supply.name
            row.findViewById<TextView>(R.id.tvAlertCategory).text = supply.category
            row.findViewById<TextView>(R.id.tvAlertQty).text =
                "${formatQty(supply.quantity)} ${supply.unit}"
            llLowStockAlerts.addView(row)
        }
    }

    override fun showRecentActivity(entries: List<HistoryEntry>) {
        llRecentActivity.removeAllViews()
        if (entries.isEmpty()) {
            tvNoActivity.visibility = View.VISIBLE
            return
        }
        tvNoActivity.visibility = View.GONE
        entries.take(5).forEach { entry ->
            val row = LayoutInflater.from(this)
                .inflate(R.layout.item_activity_row, llRecentActivity, false)
            row.findViewById<TextView>(R.id.tvActivityName).text = entry.supplyName
            row.findViewById<TextView>(R.id.tvActivityCategory).text = entry.category
            val tvChange = row.findViewById<TextView>(R.id.tvActivityChange)
            val sign = if (entry.change > 0) "+" else ""
            tvChange.text = "$sign${formatQty(entry.change)} kg"
            tvChange.setTextColor(
                ContextCompat.getColor(this,
                    if (entry.change > 0) R.color.green_primary else R.color.red_alert)
            )
            val ivIcon = row.findViewById<ImageView>(R.id.ivActivityIcon)
            ivIcon.setImageResource(
                if (entry.change > 0) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
            )
            llRecentActivity.addView(row)
        }
    }

    override fun navigateToInventory() {
        startActivity(Intent(this, InventoryActivity::class.java))
    }

    override fun navigateToHistory() {
        startActivity(Intent(this, HistoryActivity::class.java))
    }

    override fun navigateToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    override fun navigateToAddSupply() {
        startActivity(Intent(this, AddSupplyActivity::class.java))
    }

    private fun formatQty(v: Double): String =
        if (v == kotlin.math.floor(v)) v.toInt().toString() else v.toString()
}

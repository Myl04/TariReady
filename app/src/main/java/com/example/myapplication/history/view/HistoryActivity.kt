package com.example.myapplication.history.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.TariReadyApplication
import com.example.myapplication.dashboard.view.DashboardActivity
import com.example.myapplication.data.model.HistoryEntry
import com.example.myapplication.history.contract.HistoryContract
import com.example.myapplication.history.presenter.HistoryPresenter
import com.example.myapplication.inventory.view.InventoryActivity
import com.example.myapplication.profile.view.ProfileActivity

class HistoryActivity : AppCompatActivity(), HistoryContract.View {

    private lateinit var etSearch: EditText
    private lateinit var llHistoryList: LinearLayout
    private lateinit var tvEmptyState: TextView
    private lateinit var navHome: TextView
    private lateinit var navInventory: TextView
    private lateinit var navHistory: TextView
    private lateinit var navProfile: TextView

    private lateinit var presenter: HistoryContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val app = application as TariReadyApplication
        presenter = HistoryPresenter(this, app.supplyRepository)

        bindViews()
        setupSearch()
        setupNavigation()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadHistory()
    }

    private fun bindViews() {
        etSearch      = findViewById(R.id.etHistorySearch)
        llHistoryList = findViewById(R.id.llHistoryList)
        tvEmptyState  = findViewById(R.id.tvHistoryEmpty)
        navHome       = findViewById(R.id.navHome)
        navInventory  = findViewById(R.id.navInventory)
        navHistory    = findViewById(R.id.navHistory)
        navProfile    = findViewById(R.id.navProfile)
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) =
                presenter.loadHistory(s.toString().trim())
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupNavigation() {
        navHome.setOnClickListener { presenter.onDashboardClicked() }
        navInventory.setOnClickListener { presenter.onInventoryClicked() }
        navHistory.setOnClickListener { /* already here */ }
        navProfile.setOnClickListener { presenter.onProfileClicked() }
    }

    // ─── HistoryContract.View ─────────────────────────────────────────────────

    override fun showHistory(grouped: Map<String, List<HistoryEntry>>) {
        tvEmptyState.visibility = View.GONE
        llHistoryList.removeAllViews()

        grouped.forEach { (dateLabel, entries) ->
            // Date header
            val header = LayoutInflater.from(this)
                .inflate(R.layout.item_history_header, llHistoryList, false)
            header.findViewById<TextView>(R.id.tvHistoryDateLabel).text = dateLabel
            llHistoryList.addView(header)

            // Entry rows
            entries.forEach { entry ->
                val row = LayoutInflater.from(this)
                    .inflate(R.layout.item_history_row, llHistoryList, false)
                row.findViewById<TextView>(R.id.tvHistoryName).text = entry.supplyName
                row.findViewById<TextView>(R.id.tvHistoryCategory).text = entry.category
                val tvChange = row.findViewById<TextView>(R.id.tvHistoryChange)
                val sign = if (entry.change > 0) "+" else ""
                tvChange.text = "$sign${formatQty(entry.change)} kg"
                tvChange.setTextColor(
                    ContextCompat.getColor(this,
                        if (entry.change > 0) R.color.green_primary else R.color.red_alert)
                )
                val ivIcon = row.findViewById<ImageView>(R.id.ivHistoryIcon)
                ivIcon.setImageResource(
                    if (entry.change > 0) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down
                )
                llHistoryList.addView(row)
            }
        }
    }

    override fun showEmptyState() {
        llHistoryList.removeAllViews()
        tvEmptyState.visibility = View.VISIBLE
    }

    override fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    override fun navigateToInventory() {
        startActivity(Intent(this, InventoryActivity::class.java))
        finish()
    }

    override fun navigateToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
    }

    private fun formatQty(v: Double): String =
        if (v == kotlin.math.floor(v)) v.toInt().toString() else v.toString()
}

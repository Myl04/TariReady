package com.example.myapplication.inventory.view

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
import com.example.myapplication.data.model.Supply
import com.example.myapplication.history.view.HistoryActivity
import com.example.myapplication.inventory.contract.InventoryContract
import com.example.myapplication.inventory.presenter.InventoryPresenter
import com.example.myapplication.profile.view.ProfileActivity
import com.example.myapplication.supply.view.AddSupplyActivity
import com.example.myapplication.supply.view.SupplyDetailActivity

class InventoryActivity : AppCompatActivity(), InventoryContract.View {

    private lateinit var etSearch: EditText
    private lateinit var llSupplyList: LinearLayout
    private lateinit var tvEmptyState: TextView
    private lateinit var tvItemCount: TextView
    private lateinit var btnAdd: ImageButton
    private lateinit var navHome: TextView
    private lateinit var navInventory: TextView
    private lateinit var navHistory: TextView
    private lateinit var navProfile: TextView

    private val categoryChips = mutableMapOf<String, TextView>()
    private var selectedCategory = "All"
    private val categories = listOf("All", "Feeds", "Medicines", "Vitamins", "Other")

    private lateinit var presenter: InventoryContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        val app = application as TariReadyApplication
        presenter = InventoryPresenter(this, app.supplyRepository)

        bindViews()
        setupSearch()
        setupCategoryChips()
        setupNavigation()
    }

    override fun onResume() {
        super.onResume()
        reload()
    }

    private fun bindViews() {
        etSearch     = findViewById(R.id.etSearchSupplies)
        llSupplyList = findViewById(R.id.llSupplyList)
        tvEmptyState = findViewById(R.id.tvInventoryEmpty)
        tvItemCount  = findViewById(R.id.tvItemCount)
        btnAdd       = findViewById(R.id.btnAddSupply)
        navHome      = findViewById(R.id.navHome)
        navInventory = findViewById(R.id.navInventory)
        navHistory   = findViewById(R.id.navHistory)
        navProfile   = findViewById(R.id.navProfile)

        btnAdd.setOnClickListener { presenter.onAddClicked() }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = reload()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupCategoryChips() {
        val llChips = findViewById<LinearLayout>(R.id.llCategoryChips)
        llChips.removeAllViews()
        categories.forEach { cat ->
            val chip = LayoutInflater.from(this)
                .inflate(R.layout.item_category_chip, llChips, false) as TextView
            chip.text = cat
            chip.setOnClickListener { selectCategory(cat) }
            llChips.addView(chip)
            categoryChips[cat] = chip
        }
        selectCategory("All")
    }

    private fun selectCategory(cat: String) {
        selectedCategory = cat
        categoryChips.forEach { (key, chip) ->
            if (key == cat) {
                chip.setBackgroundResource(R.drawable.chip_selected)
                chip.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                chip.setBackgroundResource(R.drawable.chip_unselected)
                chip.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
            }
        }
        reload()
    }

    private fun reload() {
        presenter.loadSupplies(selectedCategory, etSearch.text.toString().trim())
    }

    private fun setupNavigation() {
        navHome.setOnClickListener { presenter.onDashboardClicked() }
        navInventory.setOnClickListener { /* already here */ }
        navHistory.setOnClickListener { presenter.onHistoryClicked() }
        navProfile.setOnClickListener { presenter.onProfileClicked() }
    }

    // ─── InventoryContract.View ───────────────────────────────────────────────

    override fun showSupplies(items: List<Supply>) {
        tvEmptyState.visibility = View.GONE
        llSupplyList.visibility = View.VISIBLE
        tvItemCount.text = "${items.size} Items"
        llSupplyList.removeAllViews()
        items.forEach { supply ->
            val row = LayoutInflater.from(this)
                .inflate(R.layout.item_supply_row, llSupplyList, false)
            row.findViewById<TextView>(R.id.tvSupplyName).text = supply.name
            row.findViewById<TextView>(R.id.tvSupplyCategory).text = supply.category
            val tvQty = row.findViewById<TextView>(R.id.tvSupplyQty)
            tvQty.text = "${formatQty(supply.quantity)} ${supply.unit}"
            if (supply.isLowStock) {
                tvQty.setTextColor(ContextCompat.getColor(this, R.color.red_alert))
            } else {
                tvQty.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
            }
            row.setOnClickListener { presenter.onSupplyClicked(supply.id) }
            llSupplyList.addView(row)
        }
    }

    override fun showEmptyState() {
        llSupplyList.removeAllViews()
        tvEmptyState.visibility = View.VISIBLE
        tvItemCount.text = "0 Items"
    }

    override fun navigateToAddSupply() {
        startActivity(Intent(this, AddSupplyActivity::class.java))
    }

    override fun navigateToSupplyDetail(supplyId: String) {
        val intent = Intent(this, SupplyDetailActivity::class.java)
        intent.putExtra("supplyId", supplyId)
        startActivity(intent)
    }

    override fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }

    override fun navigateToHistory() {
        startActivity(Intent(this, HistoryActivity::class.java))
        finish()
    }

    override fun navigateToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
        finish()
    }

    private fun formatQty(v: Double): String =
        if (v == kotlin.math.floor(v)) v.toInt().toString() else v.toString()
}

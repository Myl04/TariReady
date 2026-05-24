package com.example.myapplication.supply.view

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.TariReadyApplication
import com.example.myapplication.extensions.showToast
import com.example.myapplication.supply.contract.SupplyContract
import com.example.myapplication.supply.presenter.AddSupplyPresenter

class AddSupplyActivity : AppCompatActivity(), SupplyContract.AddView {

    private lateinit var btnBack: ImageButton
    private lateinit var btnSave: Button
    private lateinit var etName: EditText
    private lateinit var etQuantity: EditText
    private lateinit var etThreshold: EditText
    private lateinit var etExpiryDate: EditText
    private lateinit var etSupplier: EditText
    private lateinit var etNotes: EditText
    private lateinit var llCategoryChips: LinearLayout
    private lateinit var llUnitChips: LinearLayout

    private val categories = listOf("Feeds", "Medicines", "Vitamins", "Other")
    private val units      = listOf("kg", "pcs", "packs")
    private val categoryChips = mutableMapOf<String, TextView>()
    private val unitChips     = mutableMapOf<String, TextView>()
    private var selectedCategory = ""
    private var selectedUnit     = "kg"

    private lateinit var presenter: SupplyContract.AddPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_supply)

        val app = application as TariReadyApplication
        presenter = AddSupplyPresenter(this, app.supplyRepository)

        bindViews()
        setupCategoryChips()
        setupUnitChips()

        btnBack.setOnClickListener { presenter.onBackClicked() }
        btnSave.setOnClickListener { collectAndSave() }
    }

    private fun bindViews() {
        btnBack        = findViewById(R.id.btnAddBack)
        btnSave        = findViewById(R.id.btnSave)
        etName         = findViewById(R.id.etSupplyName)
        etQuantity     = findViewById(R.id.etQuantity)
        etThreshold    = findViewById(R.id.etLowStockThreshold)
        etExpiryDate   = findViewById(R.id.etExpiryDate)
        etSupplier     = findViewById(R.id.etSupplierName)
        etNotes        = findViewById(R.id.etNotes)
        llCategoryChips = findViewById(R.id.llCategoryChips)
        llUnitChips    = findViewById(R.id.llUnitChips)
    }

    private fun setupCategoryChips() {
        llCategoryChips.removeAllViews()
        categories.forEach { cat ->
            val chip = LayoutInflater.from(this)
                .inflate(R.layout.item_category_chip, llCategoryChips, false) as TextView
            chip.text = cat
            chip.setOnClickListener { selectCategory(cat) }
            llCategoryChips.addView(chip)
            categoryChips[cat] = chip
        }
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
    }

    private fun setupUnitChips() {
        llUnitChips.removeAllViews()
        units.forEach { unit ->
            val chip = LayoutInflater.from(this)
                .inflate(R.layout.item_unit_chip, llUnitChips, false) as TextView
            chip.text = unit
            chip.setOnClickListener { selectUnit(unit) }
            llUnitChips.addView(chip)
            unitChips[unit] = chip
        }
        selectUnit("kg")
    }

    private fun selectUnit(unit: String) {
        selectedUnit = unit
        unitChips.forEach { (key, chip) ->
            if (key == unit) {
                chip.setBackgroundResource(R.drawable.chip_selected)
                chip.setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                chip.setBackgroundResource(R.drawable.chip_unselected)
                chip.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
            }
        }
    }

    private fun collectAndSave() {
        presenter.onSaveClicked(
            name              = etName.text.toString().trim(),
            category          = selectedCategory,
            quantity          = etQuantity.text.toString().trim(),
            unit              = selectedUnit,
            lowStockThreshold = etThreshold.text.toString().trim(),
            expiryDate        = etExpiryDate.text.toString().trim(),
            supplier          = etSupplier.text.toString().trim(),
            notes             = etNotes.text.toString().trim()
        )
    }

    // ─── SupplyContract.AddView ───────────────────────────────────────────────

    override fun getSelectedCategory(): String = selectedCategory

    override fun showError(message: String) = showToast(message)

    override fun onSaveSuccess() {
        showToast("Supply added successfully!")
        finish()
    }

    override fun navigateBack() = finish()
}

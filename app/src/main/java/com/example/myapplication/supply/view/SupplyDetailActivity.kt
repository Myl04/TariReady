package com.example.myapplication.supply.view

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.TariReadyApplication
import com.example.myapplication.data.model.Supply
import com.example.myapplication.extensions.showToast
import com.example.myapplication.supply.contract.SupplyContract
import com.example.myapplication.supply.presenter.SupplyDetailPresenter

class SupplyDetailActivity : AppCompatActivity(), SupplyContract.DetailView {

    private lateinit var btnBack: ImageButton
    private lateinit var btnDelete: ImageButton
    private lateinit var tvName: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvCurrentStock: TextView
    private lateinit var tvStockUnit: TextView
    private lateinit var btnRestock: Button
    private lateinit var btnUseStock: Button
    private lateinit var tvSupplier: TextView
    private lateinit var tvExpiryDate: TextView
    private lateinit var tvDateAdded: TextView

    private lateinit var presenter: SupplyContract.DetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supply_detail)

        val app = application as TariReadyApplication
        presenter = SupplyDetailPresenter(this, app.supplyRepository)

        bindViews()

        val supplyId = intent.getStringExtra("supplyId") ?: run { finish(); return }
        presenter.loadSupply(supplyId)

        btnBack.setOnClickListener { presenter.onBackClicked() }
        btnDelete.setOnClickListener { presenter.onDeleteClicked() }
        btnRestock.setOnClickListener { presenter.onRestockClicked() }
        btnUseStock.setOnClickListener { presenter.onUseStockClicked() }
    }
    private fun bindViews() {
        btnBack          = findViewById(R.id.btnDetailBack)
        btnDelete        = findViewById(R.id.btnDeleteSupply)
        tvName           = findViewById(R.id.tvDetailName)
        tvCategory       = findViewById(R.id.tvDetailCategory)
        tvCurrentStock   = findViewById(R.id.tvCurrentStockValue)
        tvStockUnit      = findViewById(R.id.tvCurrentStockUnit)
        btnRestock       = findViewById(R.id.btnRestock)
        btnUseStock      = findViewById(R.id.btnUseStock)
        tvSupplier       = findViewById(R.id.tvDetailSupplier)
        tvExpiryDate     = findViewById(R.id.tvDetailExpiryDate)
        tvDateAdded      = findViewById(R.id.tvDetailDateAdded)
    }

    override fun showSupply(supply: Supply) {
        tvName.text         = supply.name
        tvCategory.text     = supply.category
        tvCurrentStock.text = formatQty(supply.quantity)
        tvStockUnit.text    = supply.unit
        tvSupplier.text     = supply.supplier.ifEmpty { "—" }
        tvExpiryDate.text   = supply.expiryDate.ifEmpty { "—" }
        tvDateAdded.text    = supply.dateAdded
    }

    override fun showError(message: String) = showToast(message)

    override fun showMessage(message: String) = showToast(message)

    override fun showRestockDialog() {
        showAmountDialog("Restock", "Enter amount to restock") { amount ->
            presenter.onRestockConfirmed(amount)
        }
    }

    override fun showUseStockDialog() {
        showAmountDialog("Use Stock", "Enter amount used") { amount ->
            presenter.onUseStockConfirmed(amount)
        }
    }

    override fun showDeleteConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Delete Supply")
            .setMessage("Are you sure you want to delete this supply? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ -> presenter.onDeleteConfirmed() }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    override fun navigateBack() = finish()

    private fun showAmountDialog(title: String, hint: String, onConfirm: (String) -> Unit) {
        val input = EditText(this).apply {
            this.hint = hint
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or
                    android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setPadding(48, 24, 48, 24)
        }
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(input)
            .setPositiveButton("Confirm") { _, _ -> onConfirm(input.text.toString().trim()) }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun formatQty(v: Double): String =
        if (v == kotlin.math.floor(v)) v.toInt().toString() else v.toString()
}

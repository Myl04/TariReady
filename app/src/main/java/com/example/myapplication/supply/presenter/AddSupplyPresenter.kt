package com.example.myapplication.supply.presenter

import com.example.myapplication.data.model.Supply
import com.example.myapplication.data.repository.SupplyRepository
import com.example.myapplication.supply.contract.SupplyContract
import java.text.SimpleDateFormat
import java.util.*

class AddSupplyPresenter(
    private val view: SupplyContract.AddView,
    private val repository: SupplyRepository
) : SupplyContract.AddPresenter {

    override fun onSaveClicked(
        name: String,
        category: String,
        quantity: String,
        unit: String,
        lowStockThreshold: String,
        expiryDate: String,
        supplier: String,
        notes: String
    ) {
        if (name.isEmpty()) { view.showError("Please enter a supply name"); return }
        if (category.isEmpty()) { view.showError("Please select a category"); return }
        if (quantity.isEmpty()) { view.showError("Please enter a quantity"); return }
        if (unit.isEmpty()) { view.showError("Please select a unit"); return }
        if (lowStockThreshold.isEmpty()) { view.showError("Please set a low stock threshold"); return }

        val qty = quantity.toDoubleOrNull()
        if (qty == null || qty < 0) { view.showError("Please enter a valid quantity"); return }

        val threshold = lowStockThreshold.toDoubleOrNull()
        if (threshold == null || threshold < 0) { view.showError("Please enter a valid threshold"); return }

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val supply = Supply(
            id                = UUID.randomUUID().toString(),
            name              = name,
            category          = category,
            quantity          = qty,
            unit              = unit,
            lowStockThreshold = threshold,
            expiryDate        = expiryDate,
            supplier          = supplier,
            notes             = notes,
            dateAdded         = today
        )
        repository.saveSupply(supply)
        view.onSaveSuccess()
    }

    override fun onBackClicked() = view.navigateBack()
}

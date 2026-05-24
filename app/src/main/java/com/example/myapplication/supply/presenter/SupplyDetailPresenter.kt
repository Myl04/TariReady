package com.example.myapplication.supply.presenter

import com.example.myapplication.data.model.HistoryEntry
import com.example.myapplication.data.repository.SupplyRepository
import com.example.myapplication.supply.contract.SupplyContract
import java.text.SimpleDateFormat
import java.util.*

class SupplyDetailPresenter(
    private val view: SupplyContract.DetailView,
    private val repository: SupplyRepository
) : SupplyContract.DetailPresenter {

    private var currentSupplyId: String = ""

    override fun loadSupply(id: String) {
        currentSupplyId = id
        val supply = repository.getSupplyById(id)
        if (supply == null) {
            view.showError("Supply not found")
            view.navigateBack()
            return
        }
        view.showSupply(supply)
    }

    override fun onRestockClicked()  = view.showRestockDialog()
    override fun onUseStockClicked() = view.showUseStockDialog()
    override fun onDeleteClicked()   = view.showDeleteConfirmation()
    override fun onBackClicked()     = view.navigateBack()

    override fun onRestockConfirmed(amount: String) {
        val amt = amount.toDoubleOrNull()
        if (amt == null || amt <= 0) { view.showError("Enter a valid amount"); return }
        updateStock(amt, "Restocked")
    }

    override fun onUseStockConfirmed(amount: String) {
        val amt = amount.toDoubleOrNull()
        if (amt == null || amt <= 0) { view.showError("Enter a valid amount"); return }
        val supply = repository.getSupplyById(currentSupplyId) ?: return
        if (amt > supply.quantity) { view.showError("Not enough stock"); return }
        updateStock(-amt, "Used")
    }

    override fun onDeleteConfirmed() {
        repository.deleteSupply(currentSupplyId)
        view.showMessage("Supply deleted")
        view.navigateBack()
    }

    private fun updateStock(change: Double, action: String) {
        val supply = repository.getSupplyById(currentSupplyId) ?: return
        val updated = supply.copy(quantity = supply.quantity + change)
        repository.saveSupply(updated)

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val entry = HistoryEntry(
            id          = UUID.randomUUID().toString(),
            supplyId    = currentSupplyId,
            supplyName  = supply.name,
            category    = supply.category,
            change      = change,
            timestamp   = System.currentTimeMillis(),
            date        = today
        )
        repository.addHistoryEntry(entry)

        view.showMessage("$action ${formatQty(Math.abs(change))} ${supply.unit}")
        view.showSupply(updated)
    }

    private fun formatQty(v: Double): String =
        if (v == kotlin.math.floor(v)) v.toInt().toString() else v.toString()
}

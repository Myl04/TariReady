package com.example.myapplication.inventory.presenter

import com.example.myapplication.data.repository.SupplyRepository
import com.example.myapplication.inventory.contract.InventoryContract

class InventoryPresenter(
    private val view: InventoryContract.View,
    private val repository: SupplyRepository
) : InventoryContract.Presenter {

    override fun loadSupplies(filterCategory: String, query: String) {
        var list = repository.getAllSupplies()
        if (filterCategory != "All") {
            list = list.filter { it.category == filterCategory }
        }
        if (query.isNotEmpty()) {
            list = list.filter { it.name.contains(query, ignoreCase = true) }
        }
        if (list.isEmpty()) view.showEmptyState() else view.showSupplies(list)
    }

    override fun onAddClicked()                    = view.navigateToAddSupply()
    override fun onSupplyClicked(supplyId: String) = view.navigateToSupplyDetail(supplyId)
    override fun onDashboardClicked()              = view.navigateToDashboard()
    override fun onHistoryClicked()                = view.navigateToHistory()
    override fun onProfileClicked()                = view.navigateToProfile()
}

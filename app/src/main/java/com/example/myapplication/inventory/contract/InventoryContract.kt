package com.example.myapplication.inventory.contract

import com.example.myapplication.data.model.Supply

interface InventoryContract {
    interface View {
        fun showSupplies(items: List<Supply>)
        fun showEmptyState()
        fun navigateToAddSupply()
        fun navigateToSupplyDetail(supplyId: String)
        fun navigateToDashboard()
        fun navigateToHistory()
        fun navigateToProfile()
    }
    interface Presenter {
        fun loadSupplies(filterCategory: String = "All", query: String = "")
        fun onAddClicked()
        fun onSupplyClicked(supplyId: String)
        fun onDashboardClicked()
        fun onHistoryClicked()
        fun onProfileClicked()
    }
}

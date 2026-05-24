package com.example.myapplication.dashboard.contract

import com.example.myapplication.data.model.HistoryEntry
import com.example.myapplication.data.model.Supply

interface DashboardContract {

    interface View {
        fun showStats(total: Int, lowStock: Int, expiring: Int)
        fun showLowStockAlerts(items: List<Supply>)
        fun showRecentActivity(entries: List<HistoryEntry>)
        fun showUsername(name: String)
        fun navigateToInventory()
        fun navigateToHistory()
        fun navigateToProfile()
        fun navigateToAddSupply()
    }

    interface Presenter {
        fun loadDashboard()
        fun onInventoryClicked()
        fun onHistoryClicked()
        fun onProfileClicked()
        fun onAddSupplyClicked()
    }
}

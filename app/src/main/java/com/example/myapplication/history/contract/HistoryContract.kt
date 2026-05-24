package com.example.myapplication.history.contract

import com.example.myapplication.data.model.HistoryEntry

interface HistoryContract {

    interface View {
        fun showHistory(grouped: Map<String, List<HistoryEntry>>)
        fun showEmptyState()
        fun navigateToDashboard()
        fun navigateToInventory()
        fun navigateToProfile()
    }

    interface Presenter {
        fun loadHistory(query: String = "")
        fun onDashboardClicked()
        fun onInventoryClicked()
        fun onProfileClicked()
    }
}

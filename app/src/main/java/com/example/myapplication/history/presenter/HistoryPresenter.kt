package com.example.myapplication.history.presenter

import com.example.myapplication.data.repository.SupplyRepository
import com.example.myapplication.history.contract.HistoryContract

class HistoryPresenter(
    private val view: HistoryContract.View,
    private val repository: SupplyRepository
) : HistoryContract.Presenter {

    override fun loadHistory(query: String) {
        var list = repository.getAllHistory()
        if (query.isNotEmpty()) {
            list = list.filter { it.supplyName.contains(query, ignoreCase = true) }
        }
        if (list.isEmpty()) {
            view.showEmptyState()
            return
        }
        // Group by date label (e.g. "FEBRUARY 28")
        val grouped = list.groupBy { formatDateLabel(it.date) }
        view.showHistory(grouped)
    }

    override fun onDashboardClicked() = view.navigateToDashboard()
    override fun onInventoryClicked() = view.navigateToInventory()
    override fun onProfileClicked()   = view.navigateToProfile()

    private fun formatDateLabel(dateStr: String): String {
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val date = sdf.parse(dateStr) ?: return dateStr
            val out = java.text.SimpleDateFormat("MMMM d", java.util.Locale.getDefault())
            out.format(date).uppercase()
        } catch (e: Exception) {
            dateStr
        }
    }
}

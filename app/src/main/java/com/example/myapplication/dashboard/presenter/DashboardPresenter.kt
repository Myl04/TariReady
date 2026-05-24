package com.example.myapplication.dashboard.presenter

import android.content.SharedPreferences
import com.example.myapplication.dashboard.contract.DashboardContract
import com.example.myapplication.data.repository.SupplyRepository
import com.example.myapplication.extensions.getUser

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val repository: SupplyRepository,
    private val prefs: SharedPreferences
) : DashboardContract.Presenter {

    override fun loadDashboard() {
        val user = prefs.getUser()
        view.showUsername(user.fullName.ifEmpty { user.username.ifEmpty { "User" } })
        view.showStats(
            total    = repository.getTotalSuppliesCount(),
            lowStock = repository.getLowStockCount(),
            expiring = repository.getExpiringCount()
        )
        view.showLowStockAlerts(repository.getLowStockSupplies())
        view.showRecentActivity(repository.getRecentHistory(10))
    }

    override fun onInventoryClicked() = view.navigateToInventory()
    override fun onHistoryClicked()   = view.navigateToHistory()
    override fun onProfileClicked()   = view.navigateToProfile()
    override fun onAddSupplyClicked() = view.navigateToAddSupply()
}

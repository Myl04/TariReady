package com.example.myapplication.supply.contract

import com.example.myapplication.data.model.Supply

interface SupplyContract {

    // Add Supply
    interface AddView {
        fun showError(message: String)
        fun onSaveSuccess()
        fun navigateBack()
        fun getSelectedCategory(): String
    }

    interface AddPresenter {
        fun onSaveClicked(
            name: String,
            category: String,
            quantity: String,
            unit: String,
            lowStockThreshold: String,
            expiryDate: String,
            supplier: String,
            notes: String
        )
        fun onBackClicked()
    }

    //Supply Detail
    interface DetailView {
        fun showSupply(supply: Supply)
        fun showError(message: String)
        fun showRestockDialog()
        fun showUseStockDialog()
        fun showDeleteConfirmation()
        fun navigateBack()
        fun showMessage(message: String)
    }

    interface DetailPresenter {
        fun loadSupply(id: String)
        fun onRestockClicked()
        fun onUseStockClicked()
        fun onDeleteClicked()
        fun onRestockConfirmed(amount: String)
        fun onUseStockConfirmed(amount: String)
        fun onDeleteConfirmed()
        fun onBackClicked()
    }
}

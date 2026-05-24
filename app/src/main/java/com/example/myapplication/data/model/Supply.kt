package com.example.myapplication.data.model

import org.json.JSONObject

data class Supply(
    val id: String,
    val name: String,
    val category: String,
    val quantity: Double,
    val unit: String,
    val lowStockThreshold: Double,
    val expiryDate: String,
    val supplier: String,
    val notes: String,
    val dateAdded: String
) {
    val isLowStock: Boolean get() = quantity <= lowStockThreshold
    val isExpiring: Boolean get() = expiryDate.isNotEmpty() // simplified flag

    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("name", name)
        put("category", category)
        put("quantity", quantity)
        put("unit", unit)
        put("lowStockThreshold", lowStockThreshold)
        put("expiryDate", expiryDate)
        put("supplier", supplier)
        put("notes", notes)
        put("dateAdded", dateAdded)
    }

    companion object {
        fun fromJson(json: JSONObject) = Supply(
            id = json.getString("id"),
            name = json.getString("name"),
            category = json.getString("category"),
            quantity = json.getDouble("quantity"),
            unit = json.getString("unit"),
            lowStockThreshold = json.getDouble("lowStockThreshold"),
            expiryDate = json.optString("expiryDate", ""),
            supplier = json.optString("supplier", ""),
            notes = json.optString("notes", ""),
            dateAdded = json.optString("dateAdded", "")
        )
    }
}

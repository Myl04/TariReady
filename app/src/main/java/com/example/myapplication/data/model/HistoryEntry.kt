package com.example.myapplication.data.model

import org.json.JSONObject

data class HistoryEntry(
    val id: String,
    val supplyId: String,
    val supplyName: String,
    val category: String,
    val change: Double,
    val timestamp: Long,
    val date: String
) {
    val isRestock: Boolean get() = change > 0

    fun toJson(): JSONObject = JSONObject().apply {
        put("id", id)
        put("supplyId", supplyId)
        put("supplyName", supplyName)
        put("category", category)
        put("change", change)
        put("timestamp", timestamp)
        put("date", date)
    }

    companion object {
        fun fromJson(json: JSONObject) = HistoryEntry(
            id = json.getString("id"),
            supplyId = json.getString("supplyId"),
            supplyName = json.getString("supplyName"),
            category = json.getString("category"),
            change = json.getDouble("change"),
            timestamp = json.getLong("timestamp"),
            date = json.getString("date")
        )
    }
}

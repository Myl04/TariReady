package com.example.myapplication.data.repository

import android.content.SharedPreferences
import com.example.myapplication.data.model.HistoryEntry
import com.example.myapplication.data.model.Supply
import org.json.JSONArray

class SupplyRepository(private val prefs: SharedPreferences) {

    //Supplies
    fun getAllSupplies(): List<Supply> {
        val json = prefs.getString(KEY_SUPPLIES, "[]") ?: "[]"
        val array = JSONArray(json)
        return (0 until array.length()).map { Supply.fromJson(array.getJSONObject(it)) }
    }

    fun saveSupply(supply: Supply) {
        val list = getAllSupplies().toMutableList()
        val idx = list.indexOfFirst { it.id == supply.id }
        if (idx >= 0) list[idx] = supply else list.add(supply)
        persistSupplies(list)
    }

    fun deleteSupply(id: String) {
        persistSupplies(getAllSupplies().filter { it.id != id })
    }

    fun getSupplyById(id: String): Supply? = getAllSupplies().firstOrNull { it.id == id }

    private fun persistSupplies(list: List<Supply>) {
        val array = JSONArray()
        list.forEach { array.put(it.toJson()) }
        prefs.edit().putString(KEY_SUPPLIES, array.toString()).apply()
    }

    //History

    fun getAllHistory(): List<HistoryEntry> {
        val json = prefs.getString(KEY_HISTORY, "[]") ?: "[]"
        val array = JSONArray(json)
        return (0 until array.length())
            .map { HistoryEntry.fromJson(array.getJSONObject(it)) }
            .sortedByDescending { it.timestamp }
    }

    fun addHistoryEntry(entry: HistoryEntry) {
        val list = getAllHistory().toMutableList()
        list.add(0, entry)
        val array = JSONArray()
        list.forEach { array.put(it.toJson()) }
        prefs.edit().putString(KEY_HISTORY, array.toString()).apply()
    }

    //Derived stats

    fun getTotalSuppliesCount(): Int = getAllSupplies().size

    fun getLowStockCount(): Int = getAllSupplies().count { it.isLowStock }

    fun getExpiringCount(): Int = getAllSupplies().count { it.isExpiring && it.expiryDate.isNotEmpty() }

    fun getLowStockSupplies(): List<Supply> = getAllSupplies().filter { it.isLowStock }

    fun getRecentHistory(limit: Int = 10): List<HistoryEntry> = getAllHistory().take(limit)

    companion object {
        private const val KEY_SUPPLIES = "supplies_data"
        private const val KEY_HISTORY  = "history_data"
    }
}

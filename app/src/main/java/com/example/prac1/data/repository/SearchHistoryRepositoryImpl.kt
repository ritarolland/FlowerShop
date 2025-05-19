package com.example.prac1.data.repository

import android.content.SharedPreferences
import com.example.prac1.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : SearchHistoryRepository {

    private val gson = Gson()
    private val KEY_HISTORY = "history_list"

    override fun getHistory(): List<SearchEntry> {
        return try {
            val json = sharedPreferences.getString(KEY_HISTORY, null) ?: return emptyList()
            val type = object : TypeToken<List<SearchEntry>>() {}.type
            gson.fromJson<List<SearchEntry>>(json, type) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override fun addQuery(query: String) {
        val current = getHistory().toMutableList()
        current.removeAll { it.query == query }
        current.add(0, SearchEntry(query, System.currentTimeMillis()))
        val limited = current.take(10)
        saveHistory(limited)
    }

    private fun saveHistory(list: List<SearchEntry>) {
        val json = gson.toJson(list)
        sharedPreferences.edit().putString(KEY_HISTORY, json).apply()
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(KEY_HISTORY).apply()
    }
}

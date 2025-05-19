package com.example.prac1.domain.repository

import com.example.prac1.data.repository.SearchEntry

interface SearchHistoryRepository {
    fun getHistory(): List<SearchEntry>
    fun addQuery(query: String)
    fun clearHistory()
}

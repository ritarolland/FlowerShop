package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.prac1.data.repository.SearchEntry
import com.example.prac1.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class SearchViewModel @Inject constructor(
    private val repository: SearchHistoryRepository
) : ViewModel() {

    private val _history = MutableStateFlow<List<SearchEntry>>(emptyList())
    val history = _history.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        _history.value = repository.getHistory()
    }

    fun addQuery(query: String) {
        if (query.isNotEmpty()) {
            repository.addQuery(query)
            loadHistory()
        }
    }

    fun clear() {
        repository.clearHistory()
        loadHistory()
    }
}

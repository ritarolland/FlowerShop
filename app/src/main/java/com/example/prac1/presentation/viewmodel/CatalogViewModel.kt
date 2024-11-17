package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.FlowersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CatalogViewModel@Inject constructor(
    private val repository: FlowersRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    private val _allCatalogItems = MutableStateFlow<List<Flower>>(emptyList())
    val catalogItems: StateFlow<List<Flower>> = combine(
        _searchQuery,
        _allCatalogItems
    ) { query, items ->
        if (query.isBlank()) items
        else items.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadCatalogItems()
    }

    private fun loadCatalogItems() {
        viewModelScope.launch {
            repository.getCatalogItems().collect { items ->
                _allCatalogItems.value = items
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query // Обновляем запрос поиска
    }
}
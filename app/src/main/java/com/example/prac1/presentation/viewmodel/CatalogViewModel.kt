package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.FlowersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CatalogViewModel@Inject constructor(
    private val repository: FlowersRepository
) : ViewModel() {
    private val _catalogItems = MutableStateFlow<List<Flower>>(emptyList())
    val catalogItems: StateFlow<List<Flower>> = _catalogItems

    init {
        loadCatalogItems()
    }

    private fun loadCatalogItems() {
        viewModelScope.launch {
            repository.getCatalogItems().collect { items ->
                _catalogItems.value = items
            }
        }
    }
}
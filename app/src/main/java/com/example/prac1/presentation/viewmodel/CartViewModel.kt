package com.example.prac1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.FlowersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel@Inject constructor(
    private val cartRepository: CartRepository,
    private val catalogRepository: FlowersRepository
) : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    private val _catalogItems = MutableStateFlow<List<Flower>>(emptyList())
    val catalogItems = _catalogItems.asStateFlow()

    init {
        loadCatalogItems()
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            cartRepository.getCartItems().collect { items ->
                _cartItems.value = items
            }
        }
    }

    private fun loadCatalogItems() {
        viewModelScope.launch {
            catalogRepository.getCatalogItems().collect { items ->
                _catalogItems.value = items
            }
        }
    }

    fun getItemById(id: String): Flower? {
        return _catalogItems.value.find { it.id == id }
    }
}
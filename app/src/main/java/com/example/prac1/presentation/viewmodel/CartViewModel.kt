package com.example.prac1.presentation.viewmodel

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

    private val _totalCost = MutableStateFlow(0.0)
    val totalCost = _totalCost.asStateFlow()

    private val _selectedItems = MutableStateFlow<List<String>>(emptyList())
    val selectedItems = _selectedItems.asStateFlow()

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode: StateFlow<Boolean> = _isSelectionMode.asStateFlow()

    private val _catalogItems = MutableStateFlow<List<Flower>>(emptyList())

    init {
        loadCatalogItems()
        loadCartItems()
    }

    private fun updateTotalCost() {
        val cartItemsSelected = _cartItems.value.filter { it.id in _selectedItems.value }
        _totalCost.value = cartItemsSelected.sumOf { cartItem ->
            val catalogItem = _catalogItems.value.firstOrNull { it.id == cartItem.flowerId }
            catalogItem?.let { item ->
                cartItem.quantity * item.price
            } ?: 0.0
        }
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

    fun updateCartItemQuantity(itemId: String, newQuantity: Int) {
        cartRepository.updateCartItemQuantity(itemId, newQuantity)
    }

    fun placeOrder(selectedCartItems: List<CartItem>) {
        viewModelScope.launch {
            //cartRepository.placeOrder(selectedCartItems)
        }
    }

    fun toggleSelectionMode() {
        _isSelectionMode.value = !_isSelectionMode.value
        if (!_isSelectionMode.value) {
            _selectedItems.value = emptyList()
            updateTotalCost()
        }
    }

    fun toggleSelection(itemId: String) {
        _selectedItems.value = if (_selectedItems.value.contains(itemId)) {
            _selectedItems.value.filter { it != itemId }
        } else {
            _selectedItems.value + itemId
        }
        updateTotalCost()
    }
}
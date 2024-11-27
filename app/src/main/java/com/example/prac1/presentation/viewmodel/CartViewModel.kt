package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.FlowersRepository
import com.example.prac1.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.UUID
import javax.inject.Inject

class CartViewModel@Inject constructor(
    private val cartRepository: CartRepository,
    private val catalogRepository: FlowersRepository,
    private val ordersRepository: OrdersRepository
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

    fun deleteSelectedCartItems() {
        viewModelScope.launch {
            for (cartItemId in _selectedItems.value) {
                cartRepository.removeItemFromCart(cartItemId)
            }
            toggleSelectionMode()
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
        if (newQuantity == 0) cartRepository.removeItemFromCart(itemId)
        else cartRepository.updateCartItemQuantity(itemId, newQuantity)
    }

    fun placeOrder(timestamp: Timestamp) {
        viewModelScope.launch {
            val order = cartRepository.createOrder(totalPrice = _totalCost.value, timestamp = timestamp)
            if (order != null) {
                cartRepository.addOrder(order)
                for (cartItemId in _selectedItems.value) {
                    cartRepository.updateCartItemOrderId(cartItemId, order.id)
                }
                toggleSelectionMode()
            }
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
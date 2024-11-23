package com.example.prac1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderViewModel@Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val cartRepository: CartRepository
): ViewModel() {
    private val _order = MutableStateFlow<OrderDataModel?>(null)
    val order = _order.asStateFlow()

    private val _flowersMap = MutableStateFlow<Map<CartItem, Flower>>(emptyMap())
    val flowersMap = _flowersMap.asStateFlow()

    private fun loadFlower(cartItem: CartItem) {
        viewModelScope.launch {
            val flower = ordersRepository.getFlowerById(cartItem.flowerId)
            if (flower != null) {
                _flowersMap.value += (cartItem to flower)
            } else Log.d("ORDER VM", "flower null")
        }
    }

    fun loadData(orderId: String) {
        viewModelScope.launch {
            ordersRepository.getOrderById(orderId).collect { order ->
                _order.value = order
            }
            ordersRepository.getOrderItems(orderId).collect { items ->
                items.forEach { cartItem ->
                    loadFlower(cartItem)
                }
            }
        }
    }

    fun clearData() {
        _order.value = null
        _flowersMap.value = emptyMap()
    }
}
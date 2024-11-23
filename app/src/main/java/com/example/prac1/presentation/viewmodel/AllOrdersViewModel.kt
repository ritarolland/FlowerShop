package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.repository.OrdersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AllOrdersViewModel @Inject constructor(
    private val ordersRepository: OrdersRepository
): ViewModel() {
    private val _orders = MutableStateFlow<List<OrderDataModel>>(emptyList())
    val orders = _orders.asStateFlow()

    init {
        viewModelScope.launch {
            loadOrders()
        }
    }

    private suspend fun loadOrders() {
        ordersRepository.getOrders().collect { items ->
            _orders.value = items
        }
    }
}
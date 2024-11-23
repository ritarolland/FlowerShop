package com.example.prac1.domain.repository

import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    fun getOrders(): Flow<List<OrderDataModel>>
    fun getOrderById(orderId: String): Flow<OrderDataModel?>
    fun getOrderItems(orderId: String): Flow<List<CartItem>>
    suspend fun getFlowerById(flowerId: String): Flower?
}
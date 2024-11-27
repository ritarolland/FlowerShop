package com.example.prac1.domain.repository

import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.model.CartItem
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    fun addItemToCart(cartItem: CartItem, onComplete: (Boolean) -> Unit = {})
    fun updateCartItemQuantity(itemId: String, newQuantity: Int, onComplete: (Boolean) -> Unit = {})
    fun updateCartItemOrderId(itemId: String, orderId: String, onComplete: (Boolean) -> Unit = {})
    fun getCartItemByFlowerId(flowerId: String): CartItem?
    fun removeItemFromCart(itemId: String, onComplete: (Boolean) -> Unit = {})
    suspend fun createOrder(totalPrice: Double, timestamp: Timestamp): OrderDataModel?
    fun clearCart()
    fun addOrder(order: OrderDataModel)
}
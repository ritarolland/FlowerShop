package com.example.prac1.domain.repository

import com.example.prac1.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    fun addItemToCart(cartItem: CartItem, onComplete: (Boolean) -> Unit = {})
    fun updateCartItemQuantity(itemId: String, newQuantity: Int)
    fun countFlowerInCart(flowerId: String): Int
    //suspend fun removeItemFromCart(itemId: String)
}
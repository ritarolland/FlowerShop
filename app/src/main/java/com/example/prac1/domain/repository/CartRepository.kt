package com.example.prac1.domain.repository

import com.example.prac1.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    fun addItemToCart(cartItem: CartItem)
    /*fun removeItemFromCart(cartItem: CartItem)
    fun clearCart()*/
}
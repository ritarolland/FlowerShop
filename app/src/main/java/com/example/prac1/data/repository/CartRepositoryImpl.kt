package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CartRepositoryImpl : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(
        listOf(
            CartItem("1", 1),
            CartItem("2", 2),
            CartItem("3", 3),
            CartItem("4", 4)
        )
    )

    override fun getCartItems(): Flow<List<CartItem>> {
        return _cartItems
    }

    override fun addItemToCart(cartItem: CartItem) {
        val existingItem = _cartItems.value.find { it.flowerId == cartItem.flowerId }
        if (existingItem != null) {
            val updatedItems = _cartItems.value.map {
                if (it.flowerId == cartItem.flowerId) {
                    it.copy(quantity = it.quantity + cartItem.quantity)
                } else {
                    it
                }
            }
            _cartItems.value = updatedItems
        } else {
            _cartItems.value += cartItem
        }
        Log.d("AAA", _cartItems.value.toString())
    }

    override fun removeItemFromCart(cartItem: CartItem) {
        _cartItems.value = _cartItems.value.filter { it.flowerId != cartItem.flowerId }
    }

    override fun clearCart() {
        _cartItems.value = emptyList()
    }
}
package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.data.model.CartItemDataModel
import com.example.prac1.domain.auth.TokenProvider
import com.example.prac1.domain.mapper.CartItemMapper.mapToDomain
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.network.api.FlowerApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: FlowerApi,
    private val tokenProvider: TokenProvider
) : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    private val ioScope = CoroutineScope(Dispatchers.IO)

    init {
        loadCartItems()
    }


    private fun loadCartItems() {
        ioScope.launch {
            try {
                var response = api.getCartItems("Bearer ${tokenProvider.getToken()}")
                if (response.code() == 401) {
                    tokenProvider.refreshToken()
                    response = api.getCartItems("Bearer ${tokenProvider.getToken()}")
                }
                if (response.isSuccessful) {
                    val cartItems = response.body()?.map { mapToDomain(it) } ?: emptyList()
                    _cartItems.value = cartItems
                } else {
                    _cartItems.value = emptyList()
                }
            } catch (e: Exception) {
                _cartItems.value = emptyList()
            }
        }
    }

    override fun getCartItems(): StateFlow<List<CartItem>> {
        return _cartItems
    }

    override fun addItemToCart(cartItem: CartItem) {
        ioScope.launch {
            try {
                val cartItemDataModel = CartItemDataModel(
                    id = cartItem.id,
                    flower_id = cartItem.flowerId,
                    quantity = cartItem.quantity
                )

                var response = api.addCartItem("Bearer ${tokenProvider.getToken()}", cartItemDataModel)
                if (response.code() == 401) {
                    tokenProvider.refreshToken()
                    response = api.addCartItem("Bearer ${tokenProvider.getToken()}", cartItemDataModel)
                }
                if (response.isSuccessful) {
                    loadCartItems()
                } else {
                    Log.d("TAG", "Failed to add item: ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                Log.d("TAG", "Exception occurred: ${e.message}")
            }
        }
    }

    override fun removeItemFromCart(cartItem: CartItem) {
        _cartItems.value = _cartItems.value.filter { it.flowerId != cartItem.flowerId }
    }

    override fun clearCart() {
        _cartItems.value = emptyList()
    }
}
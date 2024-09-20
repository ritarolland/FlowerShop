package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.data.model.CartItemDataModel
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

class CartRepositoryImpl@Inject constructor(private val api: FlowerApi) : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    private val ioScope = CoroutineScope(Dispatchers.IO)

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        ioScope.launch {
            try {
                val response = api.getCartItems()
                if (response.isSuccessful) {
                    val cartItems = response.body()?.map { mapToDomain(it) } ?: emptyList()
                    Log.d("VERONIKA", cartItems.toString())
                    _cartItems.value = cartItems
                } else {
                    Log.d("VERONIKA", "response is not successful")
                    _cartItems.value = emptyList()
                }
            } catch (e: Exception) {
                Log.d("VERONIKA", "exception")
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

                val response = api.addCartItem(cartItemDataModel)

                if (response.isSuccessful) {
                    // Обновляем локальное состояние, чтобы отразить добавление элемента
                    //_cartItems.value += cartItem
                    loadCartItems()
                    Log.d("TAG", "Item added successfully")
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
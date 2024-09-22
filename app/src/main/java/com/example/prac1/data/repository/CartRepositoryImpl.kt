package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.domain.mapper.CartItemMapper
import com.example.prac1.domain.mapper.CartItemMapper.mapToDomain
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.network.api.FlowerApi
import com.example.prac1.network.requests.RefreshTokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: FlowerApi,
    private val tokenRepository: TokenRepository
) : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    private val ioScope = CoroutineScope(Dispatchers.IO)

    init {
        loadCartItems()
    }

    private fun getToken(): String? {
        return tokenRepository.getToken()
    }

    private suspend fun refreshToken(): Boolean {
        return try {
            val refreshToken: String = tokenRepository.getRefreshToken()!! //если нет refresh_token выбросим исключение
            val response = runBlocking {
                api.refreshToken(RefreshTokenRequest(refreshToken))
            }
            if (response.isSuccessful) {
                val newAccessToken: String = response.body()?.access_token!! //если в теле не нашли токены выбросим исключение
                val newExpiresIn: Long = response.body()?.expires_in!!
                val newRefreshToken: String = response.body()?.refresh_token!!
                tokenRepository.setToken(newAccessToken, newExpiresIn)
                tokenRepository.setRefreshToken(newRefreshToken)
                true
            } else {
                false //запрос неуспешен
            }
        } catch (e: Exception) {
            false
        }
    }


    private fun loadCartItems() {
        ioScope.launch {
            try {
                var response = api.getCartItems("Bearer ${getToken()}")
                if (response.code() == 401) {
                    refreshToken()
                    response = api.getCartItems("Bearer ${getToken()}")
                }
                if (response.isSuccessful) {
                    val cartItems = response.body()?.map { mapToDomain(it) } ?: emptyList()
                    _cartItems.value = cartItems
                } else {
                    _cartItems.value = emptyList()
                    //вывести что пользователь не авторизован
                }
            } catch (e: Exception) {
                _cartItems.value = emptyList()
                //вывести что не удается получить доступ к серверу
            }
        }
    }

    override fun getCartItems(): StateFlow<List<CartItem>> {
        return _cartItems
    }

    override fun addItemToCart(cartItem: CartItem) {
        ioScope.launch {
            val cartItemDataModel = CartItemMapper.mapToDataModel(cartItem)
            try {
                var response = api.addCartItem("Bearer ${getToken()}", cartItemDataModel)
                if (response.code() == 401) {
                    refreshToken()
                    response = api.addCartItem("Bearer ${getToken()}", cartItemDataModel)
                }
                if (response.isSuccessful) {
                    loadCartItems()
                    Log.d("ADAPTER", "cartRepo: $_cartItems")
                } else {
                    //вывести что пользователь не авторизован
                }

            } catch (e: Exception) {
                //вывести что не удается получить доступ к серверу
            }
        }
    }

    /*override fun removeItemFromCart(cartItem: CartItem) {
        _cartItems.value = _cartItems.value.filter { it.flowerId != cartItem.flowerId }
    }

    override fun clearCart() {
        _cartItems.value = emptyList()
    }*/
}
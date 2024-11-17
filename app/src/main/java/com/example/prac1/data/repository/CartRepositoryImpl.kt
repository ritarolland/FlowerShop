package com.example.prac1.data.repository

import com.example.prac1.domain.mapper.CartItemMapper
import com.example.prac1.domain.mapper.CartItemMapper.mapToDomain
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.domain.repository.UserUidRepository
import com.example.prac1.data.api.FlowerApi
import com.example.prac1.data.api.requests.UpdateCartItemRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val api: FlowerApi,
    private val tokenRepository: TokenRepository,
    private val userUidRepository: UserUidRepository
) : CartRepository {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    private val ioScope = CoroutineScope(Dispatchers.IO)

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        ioScope.launch {
            tokenRepository.executeApiCall(
                apiCall = { api.getCartItems(tokenRepository.createAuthHeader()) },
                onSuccess = { response ->
                    ioScope.launch {
                        val currentUserId: String? = userUidRepository.getUserUid()
                        val cartItems = response.body()
                            ?.filter { it.user_id == currentUserId }
                            ?.map { mapToDomain(it) }
                            ?.sortedBy { it.id } ?: emptyList()
                        _cartItems.value = cartItems
                    }
                },
                onError = {
                    _cartItems.value = emptyList()
                    // вывести что пользователь не авторизован
                }
            )
        }
    }

    override fun getCartItems(): StateFlow<List<CartItem>> {
        return _cartItems
    }

    override fun addItemToCart(cartItem: CartItem) {
        ioScope.launch {
            val cartItemDataModel = CartItemMapper.mapToDataModel(cartItem)
            tokenRepository.executeApiCall(
                apiCall = { api.addCartItem(tokenRepository.createAuthHeader(), cartItemDataModel) },
                onSuccess = { loadCartItems() },
                onError = {
                    // вывести что пользователь не авторизован
                }
            )
        }
    }

    override fun updateCartItemQuantity(itemId: String, newQuantity: Int) {
        ioScope.launch {
            tokenRepository.executeApiCall(
                apiCall = {
                    api.updateCartItemQuantity(
                        itemId = "eq.$itemId",
                        token = tokenRepository.createAuthHeader(),
                        updateCartItemRequest = UpdateCartItemRequest(newQuantity.toLong())
                    )
                },
                onSuccess = { loadCartItems() },
                onError = {
                    // вывести что пользователь не авторизован
                }
            )
        }
    }

    /*override fun removeItemFromCart(cartItem: CartItem) {
        _cartItems.value = _cartItems.value.filter { it.flowerId != cartItem.flowerId }
    }

    override fun clearCart() {
        _cartItems.value = emptyList()
    }*/
}
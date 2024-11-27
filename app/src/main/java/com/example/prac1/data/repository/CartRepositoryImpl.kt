package com.example.prac1.data.repository

import com.example.prac1.data.api.FlowerApi
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.data.api.requests.OrderCartItemRequest
import com.example.prac1.data.api.requests.UpdateCartItemRequest
import com.example.prac1.domain.mapper.CartItemMapper
import com.example.prac1.domain.mapper.CartItemMapper.mapToDomain
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.domain.repository.UserUidRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.UUID
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

    override suspend fun createOrder(totalPrice: Double, timestamp: Timestamp): OrderDataModel? {
        val uid = userUidRepository.getUserUid()
        return if(uid != null) {
            OrderDataModel(
                id = UUID.randomUUID().toString(),
                user_id = uid,
                total_price = totalPrice,
                created_at = timestamp,
                address = ""
            )
        } else null
    }

    private fun loadCartItems(onComplete: (Boolean) -> Unit = {}) {
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
                        onComplete(true)
                    }
                },
                onError = {
                    _cartItems.value = emptyList()
                    // вывести что пользователь не авторизован
                    onComplete(false)
                }
            )
        }
    }

    override fun getCartItems(): StateFlow<List<CartItem>> {
        return _cartItems
    }

    override fun addItemToCart(cartItem: CartItem, onComplete: (Boolean) -> Unit) {
        ioScope.launch {
            val cartItemDataModel = CartItemMapper.mapToDataModel(cartItem)
            tokenRepository.executeApiCall(
                apiCall = {
                    api.addCartItem(
                        tokenRepository.createAuthHeader(),
                        cartItemDataModel
                    )
                },
                onSuccess = {
                    loadCartItems(onComplete = onComplete)
                },
                onError = {
                    onComplete(false)
                }
            )
        }
    }

    override fun updateCartItemQuantity(itemId: String, newQuantity: Int, onComplete: (Boolean) -> Unit) {
        ioScope.launch {
            tokenRepository.executeApiCall(
                apiCall = {
                    api.updateCartItemQuantity(
                        itemId = "eq.$itemId",
                        token = tokenRepository.createAuthHeader(),
                        updateCartItemRequest = UpdateCartItemRequest(newQuantity.toLong())
                    )
                },
                onSuccess = { loadCartItems(onComplete = onComplete) },
                onError = {
                    // вывести что пользователь не авторизован
                }
            )
        }
    }

    override fun updateCartItemOrderId(
        itemId: String,
        orderId: String,
        onComplete: (Boolean) -> Unit
    ) {
        ioScope.launch {
            tokenRepository.executeApiCall(
                apiCall = {
                    api.orderCartItem(
                        token = tokenRepository.createAuthHeader(),
                        itemId = "eq.$itemId",
                        orderCartItemRequest = OrderCartItemRequest(orderId)
                    )
                },
                onSuccess = {
                    loadCartItems(onComplete = onComplete)
                }
            )
        }
    }

    override fun getCartItemByFlowerId(flowerId: String): CartItem? {
        return _cartItems.value.find { it.flowerId == flowerId }
    }

    override fun removeItemFromCart(itemId: String, onComplete: (Boolean) -> Unit) {
        ioScope.launch {
            tokenRepository.executeApiCall(
                apiCall = {
                    api.deleteItemFromCart(tokenRepository.createAuthHeader(), "eq.$itemId")
                },
                onSuccess = {
                    loadCartItems(onComplete = onComplete) },
                onError = {
                    // отправить в tokenRepository что пользователь не авторизован
                }
            )
        }
    }

    override fun clearCart() {

    }

    override fun addOrder(order: OrderDataModel) {
        ioScope.launch {
            tokenRepository.executeApiCall(
                apiCall = {
                    api.addOrder(tokenRepository.createAuthHeader(), order)
                }
            )
        }
    }
}
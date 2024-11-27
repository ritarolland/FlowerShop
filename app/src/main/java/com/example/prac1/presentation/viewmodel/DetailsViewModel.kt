package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.FlowersRepository
import com.example.prac1.domain.repository.UserUidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val catalogRepository: FlowersRepository,
    private val cartRepository: CartRepository,
    private val userUidRepository: UserUidRepository
) : ViewModel() {

    private val _selectedItem = MutableStateFlow<Flower?>(null)
    val selectedItem = _selectedItem.asStateFlow()
    private val _cartItem = MutableStateFlow<CartItem?>(null)
    val flowerCountInCart = _cartItem.asStateFlow()

    fun loadItemById(itemId: String) {
        viewModelScope.launch {
            val items = catalogRepository.getCatalogItems()
            items.collect { itemList ->
                _selectedItem.value = itemList.find { it.id == itemId } ?: Flower()
                updateCartItem()
            }
        }
    }

    private suspend fun updateCartItem() {
        _cartItem.emit(cartRepository.getCartItemByFlowerId(_selectedItem.value?.id ?: ""))
    }


    fun clearSelectedItem() {
        _selectedItem.value = null
    }

    fun addItemToCart() {
        viewModelScope.launch {
            val uid = userUidRepository.getUserUid() ?: return@launch
            if (_selectedItem.value != null) {
                val cartItem = CartItem(
                    id = UUID.randomUUID().toString(),
                    flowerId = _selectedItem.value!!.id,
                    userId = uid,
                    quantity = 1
                )
                cartRepository.addItemToCart(cartItem) { success ->
                    if (success) {
                        viewModelScope.launch {
                            updateCartItem()
                        }
                    } else {
                        viewModelScope.launch {
                            updateCartItem()
                        }
                    }
                }
            }
        }
    }

    fun updateCartItemQuantity(itemId: String, newQuantity: Int) {
        if (newQuantity == 0) {
            deleteItemFromCart(itemId)
        } else {
            cartRepository.updateCartItemQuantity(
                itemId,
                newQuantity,
                onComplete = { isSuccessful ->
                    if (isSuccessful) {
                        viewModelScope.launch {
                            updateCartItem()
                        }
                    }
                })
        }
    }

    private fun deleteItemFromCart(itemId: String) {
        cartRepository.removeItemFromCart(itemId) { isSuccessful ->
            if (isSuccessful) {
                viewModelScope.launch {
                    updateCartItem()
                }
            }
        }
    }
}
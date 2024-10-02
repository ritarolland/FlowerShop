package com.example.prac1.presentation.viewmodel

import android.util.Log
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

    fun loadItemById(itemId: String) {
        viewModelScope.launch {
            val items = catalogRepository.getCatalogItems()
            items.collect { itemList ->
                _selectedItem.value = itemList.find { it.id == itemId }
            }
        }
    }

    fun addItemToCart(item: Flower) {
        viewModelScope.launch {
            val uid = userUidRepository.getUserUid()
            if (uid == null) {
                Log.d("DETAIL", "uid null")
                return@launch
            }
            val cartItem = CartItem(id = UUID.randomUUID().toString(), flowerId = item.id, userId = uid, quantity = 2)
            cartRepository.addItemToCart(cartItem)
        }
    }
}
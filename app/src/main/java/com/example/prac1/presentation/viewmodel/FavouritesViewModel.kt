package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.FavouritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavouritesViewModel @Inject constructor(
    private val favouritesRepository: FavouritesRepository
): ViewModel() {
    private val _favouriteFlowers = MutableStateFlow<List<Flower>>(emptyList())
    val favouriteFlowers = _favouriteFlowers.asStateFlow()

    init {
        loadFavouriteFlowers()
    }

    private fun loadFavouriteFlowers() {
        viewModelScope.launch {
            favouritesRepository.getFavouriteFlowers().collect{ items ->
                _favouriteFlowers.value = items
            }
        }
    }

    fun toggleIsFavourite(flowerId: String) {
        favouritesRepository.toggleIsFavourite(flowerId)
    }
}
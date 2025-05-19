package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.repository.SearchResult
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.FavouritesRepository
import com.example.prac1.domain.repository.FlowersRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CatalogViewModel@Inject constructor(
    private val repository: FlowersRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()
    private val _searchResult = MutableStateFlow<SearchResult>(SearchResult.Default)
    val searchResult = _searchResult.asStateFlow()
    private val _allCatalogItems = MutableStateFlow<List<Flower>>(emptyList())
    private val _foundItems = MutableStateFlow<List<Flower>>(emptyList())
    private val _favourites = MutableStateFlow<List<String>>(emptyList())
    val favourites = _favourites.asStateFlow()
    val catalogItems: StateFlow<List<Flower>> = combine(
        _allCatalogItems,
        _foundItems
    ) { items, foundItems ->
        if (_searchResult.value is SearchResult.Default) items
        else foundItems
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadCatalogItems()
        loadFavourites()
    }

    fun loadFoundItems(query: String) {
        viewModelScope.launch {
            _searchResult.value = SearchResult.Loading
            repository.search(query).collect { items ->
                if (items == null) _searchResult.value = SearchResult.Error
                else {
                    _searchResult.value = SearchResult.Success
                    _foundItems.value = items
                }
            }
        }
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            favouritesRepository.getFavouriteFlowers().collect { items ->
                _favourites.value = items.map { it.id }
            }
        }
    }

    private fun loadCatalogItems() {
        viewModelScope.launch {
            repository.getCatalogItems().collect { items ->
                _allCatalogItems.value = items
            }
        }
    }

    fun toggleIsFavourite(flowerId: String) {
        favouritesRepository.toggleIsFavourite(flowerId)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query == "") _searchResult.value = SearchResult.Default
        loadFoundItems(query)
    }

    fun isFavorite(id: String): Boolean {
        return id in favourites.value
    }
}
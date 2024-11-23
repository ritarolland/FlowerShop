package com.example.prac1.domain.repository

import com.example.prac1.domain.model.Flower
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun getFavouriteFlowers(): Flow<List<Flower>>
    fun toggleIsFavourite(flowerId: String)
}
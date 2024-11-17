package com.example.prac1.domain.repository

import com.example.prac1.domain.model.Flower
import kotlinx.coroutines.flow.Flow

interface FavouritesRepository {
    fun getFavourites(): Flow<List<Flower>>
    fun addFavourite(flowerId: String)
}
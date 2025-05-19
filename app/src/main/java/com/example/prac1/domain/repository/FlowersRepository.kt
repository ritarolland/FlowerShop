package com.example.prac1.domain.repository

import com.example.prac1.domain.model.Flower
import kotlinx.coroutines.flow.Flow

interface FlowersRepository {
    suspend fun getCatalogItems(): Flow<List<Flower>>
    suspend fun search(query: String): Flow<List<Flower>?>
    suspend fun saveFlowersToCache(flowers: List<Flower>)
}
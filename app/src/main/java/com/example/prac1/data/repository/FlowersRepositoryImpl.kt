package com.example.prac1.data.repository

import com.example.prac1.data.api.FlowerApi
import com.example.prac1.data.db.FlowerDao
import com.example.prac1.domain.mapper.FlowerMapper.mapToDomain
import com.example.prac1.domain.mapper.FlowerMapper.mapToEntity
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.FlowersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FlowersRepositoryImpl@Inject constructor(
    private val api: FlowerApi,
    private val flowerDao: FlowerDao
) : FlowersRepository {

    override suspend fun getCatalogItems(): Flow<List<Flower>> {
        return flow {
            try {
                val response = api.getFlowersCatalog()
                if (response.isSuccessful) {
                    val flowers = response.body()?.map { mapToDomain(it) } ?: emptyList()
                    saveFlowersToCache(flowers)
                    emit(flowers)
                } else {
                    emitAll(getCachedFlowers())
                }
            } catch (e: Exception) {
                emitAll(getCachedFlowers())
            }
        }
    }

    override suspend fun search(query: String): Flow<List<Flower>?> {
        return flow {
            try {
                val response = api.search(name = "ilike.*$query*")
                if (response.isSuccessful) {
                    val flowers = response.body()?.map { mapToDomain(it) } ?: emptyList()
                    emit(flowers)
                } else {
                    emit(null)
                }
            } catch (e: Exception) {
                emit(null)
            }
        }
    }

    override suspend fun saveFlowersToCache(flowers: List<Flower>) {
        val flowerEntities = flowers.map { mapToEntity(it) }
        flowerDao.saveFlowers(flowerEntities)
    }

    private fun getCachedFlowers(): Flow<List<Flower>> {
        return flowerDao.getCachedFlowers().map { entities ->
            entities.map { mapToDomain(it) }
        }
    }
}
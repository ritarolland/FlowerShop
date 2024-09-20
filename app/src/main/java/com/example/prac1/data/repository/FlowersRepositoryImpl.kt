package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.domain.mapper.FlowerMapper.mapToDomain
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.FlowersRepository
import com.example.prac1.network.api.FlowerApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FlowersRepositoryImpl@Inject constructor(private val api: FlowerApi) : FlowersRepository {

    override fun getCatalogItems(): Flow<List<Flower>> {
        return flow {
            try {
                val response = api.getFlowersCatalog()
                if (response.isSuccessful) {
                    val flowers = response.body()?.map { mapToDomain(it) } ?: emptyList()
                    Log.d("VERONIKA", flowers.toString())
                    emit(flowers)
                } else {
                    Log.d("VERONIKA", "response is not successful")
                    emit(emptyList())
                }
            } catch (e: Exception) {
                Log.d("VERONIKA", "exception")
                emit(emptyList())
            }
        }
    }
}
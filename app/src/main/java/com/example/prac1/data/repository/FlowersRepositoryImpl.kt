package com.example.prac1.data.repository

import com.example.prac1.domain.repository.FlowersRepository
import com.example.prac1.domain.model.Flower
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FlowersRepositoryImpl : FlowersRepository {
    override fun getCatalogItems(): Flow<List<Flower>> {
        return flow {
            val dummyItems = listOf(
                Flower("1", "Роза", "", 100.0),
                Flower("2", "Тюльпан", "", 80.0),
                Flower("3", "Орхидея", "", 150.0),
                Flower("4", "Подсолнух", "", 50.0)
            )
            emit(dummyItems)
        }
    }
}
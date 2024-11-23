package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.data.api.FlowerApi
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.domain.mapper.CartItemMapper
import com.example.prac1.domain.mapper.FlowerMapper
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.model.Flower
import com.example.prac1.domain.repository.OrdersRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.domain.repository.UserUidRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OrdersRepositoryImpl @Inject constructor(
    private val api: FlowerApi,
    private val uidRepository: UserUidRepository,
    private val tokenRepository: TokenRepository
) : OrdersRepository {

    override fun getOrders(): Flow<List<OrderDataModel>> {
        return flow {
            val response = tokenRepository.executeApiCall {
                api.getOrders(
                    tokenRepository.createAuthHeader(),
                    userId = "eq.${uidRepository.getUserUid()}"
                )
            }
            if (response != null) {
                emit(response.body() ?: emptyList())
            } else {
                emit(emptyList())
            }
        }
    }

    override fun getOrderById(orderId: String): Flow<OrderDataModel?> {
        return flow {
            val response = tokenRepository.executeApiCall { api.getOrderById(
                tokenRepository.createAuthHeader(),
                "eq.$orderId"
            ) }
            if (response != null) {
                emit(response.body()?.firstOrNull())
            } else {
                emit(null)
            }
        }
    }


    override fun getOrderItems(orderId: String): Flow<List<CartItem>> {
        return flow {
            val response = tokenRepository.executeApiCall {
                api.getOrderItems(
                    tokenRepository.createAuthHeader(),
                    orderId = "eq.$orderId"
                )
            }
            if (response != null) {
                Log.d("ORDERS", "Current order items: ${response.body()}")
                emit(response.body()?.map { CartItemMapper.mapToDomain(it) } ?: emptyList())
            } else {
                Log.d("ORDERS", "Current order items: null")
                emit(emptyList())
            }
        }
    }

    override suspend fun getFlowerById(flowerId: String): Flower? {
        val response = tokenRepository.executeApiCall { api.getFlowerById(tokenRepository.createAuthHeader(), "eq.$flowerId") }
        response?.body()?.let {
            return response.body()?.map { FlowerMapper.mapToDomain(it) }?.firstOrNull()
        }
        return null
    }
}
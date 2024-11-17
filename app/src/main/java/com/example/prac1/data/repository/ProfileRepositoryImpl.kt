package com.example.prac1.data.repository

import com.example.prac1.data.api.model.UserInfoDataModel
import com.example.prac1.domain.repository.ProfileRepository
import com.example.prac1.domain.repository.UserUidRepository
import com.example.prac1.data.api.FlowerApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: FlowerApi,
    private val userUidRepository: UserUidRepository
) : ProfileRepository {
    override suspend fun getUserInfo(): Flow<UserInfoDataModel?> {
        return flow {
            try {
                val response = api.getUserInfo(
                    userId = "eq.${userUidRepository.getUserUid()}"
                )
                if (response.isSuccessful) {
                    val userInfo = response.body()
                    emit(userInfo?.firstOrNull())
                } else {
                    emit(null)
                }
            } catch (e: Exception) {
                emit(null)
            }
        }
    }
}
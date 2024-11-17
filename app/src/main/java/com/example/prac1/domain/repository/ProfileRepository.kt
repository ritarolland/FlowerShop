package com.example.prac1.domain.repository

import com.example.prac1.data.api.model.UserInfoDataModel
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserInfo(): Flow<UserInfoDataModel?>
}
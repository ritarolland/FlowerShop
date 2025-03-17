package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.data.api.model.UserInfoDataModel
import com.example.prac1.domain.repository.ProfileRepository
import com.example.prac1.domain.repository.UserUidRepository
import com.example.prac1.data.api.FlowerApi
import com.example.prac1.domain.model.CartItem
import com.example.prac1.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: FlowerApi,
    private val userUidRepository: UserUidRepository,
    private val tokenRepository: TokenRepository
) : ProfileRepository {
    private val _userInfo = MutableStateFlow<UserInfoDataModel?>(null)
    private val ioScope = CoroutineScope(Dispatchers.IO)

    init {
        ioScope.launch {
            loadUserInfo()
        }
    }

    override fun fetchInfo() {
        ioScope.launch {
            loadUserInfo()
        }
    }

    private suspend fun loadUserInfo() {
        tokenRepository.executeApiCall(
            apiCall = {
                val uid = userUidRepository.getUserUid()
                api.getUserInfo(userId = "eq.$uid")
            },
            onSuccess = { response ->
                val userInfo = response.body()
                _userInfo.value = userInfo?.firstOrNull()
            }
        )
    }

    override suspend fun getUserInfo(): Flow<UserInfoDataModel?> {
        return _userInfo
    }
}
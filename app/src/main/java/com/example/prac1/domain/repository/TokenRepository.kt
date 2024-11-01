package com.example.prac1.domain.repository

import retrofit2.Response

interface TokenRepository {
    suspend fun refreshToken(): Boolean
    suspend fun <T> executeApiCall(
        apiCall: suspend () -> Response<T>,
        onSuccess: (Response<T>) -> Unit,
        onError: () -> Unit
    )
    fun createAuthHeader(): String
    fun getToken(): String?
    fun setToken(token: String, expiresIn: Long)
    fun clearToken()
    fun getRefreshToken(): String?
    fun setRefreshToken(refreshToken: String)
    fun clearRefreshToken()
    fun isTokenExpired(): Boolean
}
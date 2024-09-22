package com.example.prac1.domain.repository

interface TokenRepository {
    fun getToken(): String?
    fun setToken(token: String, expiresIn: Long)
    fun clearToken()
    fun getRefreshToken(): String?
    fun setRefreshToken(refreshToken: String)
    fun clearRefreshToken()
    fun isTokenExpired(): Boolean
}
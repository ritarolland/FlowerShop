package com.example.prac1.domain.repository

interface TokenRepository {
    fun getToken(): String
    fun setToken(token: String)
    fun getRefreshToken(): String
    fun setRefreshToken(refreshToken: String)
}
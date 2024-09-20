package com.example.prac1.data.repository

import com.example.prac1.domain.repository.TokenRepository

class TokenRepositoryImpl : TokenRepository {
    private var token: String = ""
    private var refreshToken: String = ""

    override fun getToken(): String {
        return token
    }

    override fun setToken(token: String) {
        this.token = token
    }

    override fun getRefreshToken(): String {
        return refreshToken
    }

    override fun setRefreshToken(refreshToken: String) {
        this.refreshToken = refreshToken
    }
}
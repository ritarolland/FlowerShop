package com.example.prac1.data.repository

import com.example.prac1.data.api.FlowerApi
import com.example.prac1.data.api.requests.LoginRequest
import com.example.prac1.domain.repository.AuthRepository
import com.example.prac1.domain.repository.TokenRepository

class AuthRepositoryImpl(
    private val api: FlowerApi,
    private val tokenRepository: TokenRepository
) : AuthRepository {

     override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            val response = api.signIn(LoginRequest(email, password))
            if (response.isSuccessful) {
                val accessToken: String = response.body()?.access_token!!
                val expiresIn: Long = response.body()?.expires_in!!
                val refreshToken: String = response.body()?.refresh_token!!
                tokenRepository.setToken(accessToken, expiresIn)
                tokenRepository.setRefreshToken(refreshToken)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            val response = api.signUp(LoginRequest(email, password))
            if (response.isSuccessful) {
                val result = signIn(email, password)
                result
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun logOut() {
        tokenRepository.clearToken()
        tokenRepository.clearRefreshToken()
    }

    override suspend fun isUserAuthorized(): Boolean {
        if (tokenRepository.getToken() == null && tokenRepository.getRefreshToken() == null) return false
        if (tokenRepository.isTokenExpired()) {
            val successfullyRefreshed = tokenRepository.refreshToken()
            return successfullyRefreshed
        } else {
            return true
        }
    }

}
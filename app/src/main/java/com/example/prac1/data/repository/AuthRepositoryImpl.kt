package com.example.prac1.data.repository

import com.example.prac1.domain.repository.AuthRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.network.api.FlowerApi
import com.example.prac1.network.requests.LoginRequest
import com.example.prac1.network.requests.RefreshTokenRequest
import kotlinx.coroutines.runBlocking

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

    private suspend fun refreshToken(): Boolean {
        return try {
            val refreshToken: String = tokenRepository.getRefreshToken()!! //если нет refresh_token выбросим исключение
            val response = runBlocking {
                api.refreshToken(RefreshTokenRequest(refreshToken))
            }
            if (response.isSuccessful) {
                val newAccessToken: String = response.body()?.access_token!! //если в теле не нашли токены выбросим исключение
                val newExpiresIn: Long = response.body()?.expires_in!!
                val newRefreshToken: String = response.body()?.refresh_token!!
                tokenRepository.setToken(newAccessToken, newExpiresIn)
                tokenRepository.setRefreshToken(newRefreshToken)
                true
            } else {
                false //запрос неуспешен
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isUserAuthorized(): Boolean{
        if (tokenRepository.isTokenExpired()) {
            val successfullyRefreshed = refreshToken()
            return successfullyRefreshed
        } else {
            return true
        }
    }

}
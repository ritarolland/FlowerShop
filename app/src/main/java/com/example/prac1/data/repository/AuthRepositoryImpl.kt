package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.domain.repository.AuthRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.network.api.FlowerApi
import com.example.prac1.network.requests.LoginRequest

class AuthRepositoryImpl(
    private val api: FlowerApi,
    private val tokenRepository: TokenRepository
) : AuthRepository {

     override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            val response = api.signIn(LoginRequest(email, password))

            if (response.isSuccessful) {
                val accessToken = response.body()?.access_token ?: ""
                val refreshToken = response.body()?.refresh_token ?: ""

                tokenRepository.setToken(accessToken)
                tokenRepository.setRefreshToken(refreshToken)

                true
            } else {
                Log.d("QWERTY", "response is not successful")
                false
            }
        } catch (e: Exception) {
            Log.d("QWERTY", "exception ${e.message}")
            false
        }
    }
}
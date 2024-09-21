
package com.example.prac1.data.auth

import com.example.prac1.domain.auth.TokenProvider
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.network.api.FlowerApi
import com.example.prac1.network.requests.RefreshTokenRequest
import kotlinx.coroutines.runBlocking

class TokenProviderImpl(
    private val api: FlowerApi,
    private val tokenRepository: TokenRepository
) : TokenProvider {




    override fun getToken(): String {
        return tokenRepository.getToken()
    }

    override fun refreshToken(): Boolean {
        return try {
            val refreshToken = tokenRepository.getRefreshToken()
            val response = runBlocking {
                api.refreshToken(RefreshTokenRequest(refreshToken))
            }

            if (response.isSuccessful) {
                val newAccessToken = response.body()?.access_token ?: ""
                val newRefreshToken = response.body()?.refresh_token ?: ""
                tokenRepository.setToken(newAccessToken)
                tokenRepository.setRefreshToken(newRefreshToken)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}

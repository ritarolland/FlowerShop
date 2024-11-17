package com.example.prac1.data.repository

import android.content.SharedPreferences
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.data.api.FlowerApi
import com.example.prac1.data.api.requests.RefreshTokenRequest
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class TokenRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val api: FlowerApi
) : TokenRepository {
    override suspend fun refreshToken(): Boolean {
        return try {
            val refreshToken = getRefreshToken() ?: return false
            val response = runBlocking {
                api.refreshToken(RefreshTokenRequest(refreshToken))
            }
            if (response.isSuccessful) {
                val newAccessToken = response.body()?.access_token ?: return false
                val newExpiresIn = response.body()?.expires_in ?: return false
                val newRefreshToken = response.body()?.refresh_token ?: return false
                setToken(newAccessToken, newExpiresIn)
                setRefreshToken(newRefreshToken)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun <T> apiCallWithRetry(
        call: suspend () -> Response<T>
    ): Response<T> {
        val response = call()
        return if ((response.code() == 401 || response.code() == 403) && refreshToken()) {
            call()
        } else {
            response
        }
    }

    override suspend fun <T> executeApiCall(
        apiCall: suspend () -> Response<T>,
        onSuccess: (Response<T>) -> Unit,
        onError: () -> Unit
    ) {
        try {
            val response = apiCallWithRetry(apiCall)
            if (response.isSuccessful) {
                onSuccess(response)
            } else {
                onError()
            }
        } catch (e: Exception) {
            onError()
        }
    }

    override fun createAuthHeader(): String = "Bearer ${getToken()}"


    override fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    override fun setToken(token: String, expiresIn: Long) {
        val expirationTime = (System.currentTimeMillis() / 1000) + expiresIn
        sharedPreferences.edit().putString("access_token", token).apply()
        sharedPreferences.edit().putLong("expires_in", expirationTime).apply()
    }

    override fun clearToken() {
        sharedPreferences.edit().remove("access_token").apply()
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString("refresh_token", null)
    }

    override fun setRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString("refresh_token", refreshToken).apply()
    }

    override fun clearRefreshToken() {
        sharedPreferences.edit().remove("refresh_token").apply()
    }

    override fun isTokenExpired(): Boolean {
        val tokenExpirationTime = sharedPreferences.getLong("expires_in", 0)
        val currentTime = System.currentTimeMillis() / 1000
        return currentTime >= tokenExpirationTime
    }
}
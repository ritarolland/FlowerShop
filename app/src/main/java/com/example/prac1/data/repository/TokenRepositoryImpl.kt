package com.example.prac1.data.repository

import android.content.SharedPreferences
import com.example.prac1.domain.repository.TokenRepository

class TokenRepositoryImpl(private val sharedPreferences: SharedPreferences) : TokenRepository {

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
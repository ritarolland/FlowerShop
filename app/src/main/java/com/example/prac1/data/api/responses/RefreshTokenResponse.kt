package com.example.prac1.data.api.responses

data class RefreshTokenResponse(
    val access_token: String,
    val refresh_token: String,
    val expires_in: Long
)
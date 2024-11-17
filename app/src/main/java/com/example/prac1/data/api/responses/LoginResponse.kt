package com.example.prac1.data.api.responses

import com.example.prac1.data.api.model.UserDataModel

data class LoginResponse(
    val access_token: String,
    val expires_in: Long,
    val refresh_token: String,
    val user: UserDataModel
)
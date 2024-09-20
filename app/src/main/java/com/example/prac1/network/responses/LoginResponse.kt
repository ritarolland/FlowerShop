package com.example.prac1.network.responses

import com.example.prac1.data.model.UserDataModel

data class LoginResponse(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val user: UserDataModel
)
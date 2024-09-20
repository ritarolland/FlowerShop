package com.example.prac1.network.requests

data class LoginRequest(
    private val email: String,
    private val password: String
)
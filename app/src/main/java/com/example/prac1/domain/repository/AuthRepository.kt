package com.example.prac1.domain.repository

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Boolean
}
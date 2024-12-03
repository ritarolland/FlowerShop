package com.example.prac1.domain.repository

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun isUserAuthorized(): Boolean
    fun logOut()
}
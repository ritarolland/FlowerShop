package com.example.prac1.domain.auth

interface TokenProvider {
    fun refreshToken(): Boolean
    fun getToken(): String
}
package com.example.prac1.domain.auth

interface TokenRetriever {
    fun getToken(): String
}
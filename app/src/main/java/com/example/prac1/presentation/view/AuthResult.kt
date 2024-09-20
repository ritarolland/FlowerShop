package com.example.prac1.presentation.view


sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val exception: Throwable) : AuthResult<Nothing>()
    data object Loading : AuthResult<Nothing>()
}
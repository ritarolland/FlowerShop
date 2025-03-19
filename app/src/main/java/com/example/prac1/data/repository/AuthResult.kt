package com.example.prac1.data.repository

sealed class AuthState {
    data object Default : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}


sealed class AuthResult<out T> {
    data class Success<out T>(val data: T) : AuthResult<T>()
    data class Error(val exception: Throwable) : AuthResult<Nothing>()
    data object Loading : AuthResult<Nothing>()
}
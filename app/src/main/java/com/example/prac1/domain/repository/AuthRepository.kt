package com.example.prac1.domain.repository

import com.example.prac1.data.api.model.UserInfoDataModel
import com.example.prac1.data.repository.AuthState
import java.io.File

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AuthState
    suspend fun signUp(email: String, password: String): AuthState
    suspend fun isUserAuthorized(): Boolean
    fun logOut()
    suspend fun uploadImageToSupabase(imageFile: File, fileName: String): String?
    suspend fun uploadUserInfo(name: String, imageUrl: String?, email: String): AuthState
}
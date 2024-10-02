package com.example.prac1.domain.repository

interface UserUidRepository {
    suspend fun getUserUid(): String?
    fun setUserUid(uid: String)
    fun clearUid()
}
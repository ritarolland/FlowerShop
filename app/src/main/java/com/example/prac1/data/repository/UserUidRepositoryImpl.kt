package com.example.prac1.data.repository

import android.content.SharedPreferences
import com.example.prac1.data.api.FlowerApi
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.domain.repository.UserUidRepository
import javax.inject.Inject

class UserUidRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val tokenRepository: TokenRepository,
    private val api: FlowerApi
) : UserUidRepository {

    override suspend fun getUserUid(): String? {
        val uid = sharedPreferences.getString("user_uid", null)
        if (uid == null) {
            var newUid: String? = null
            tokenRepository.executeApiCall(
                apiCall = {api.getUser(tokenRepository.createAuthHeader())},
                onSuccess = { response ->
                    newUid = response.body()?.id!!
                },
                onError = {
                    newUid = null
                }
            )
            return newUid
        } else return uid
    }

    override fun setUserUid(uid: String) {
        sharedPreferences.edit().putString("user_uid", uid).apply()
    }

    override fun clearUid() {
        sharedPreferences.edit().remove("user_uid").apply()
    }
}
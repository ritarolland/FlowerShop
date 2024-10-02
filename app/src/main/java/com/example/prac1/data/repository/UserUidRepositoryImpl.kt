package com.example.prac1.data.repository

import android.content.SharedPreferences
import android.util.Log
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.domain.repository.UserUidRepository
import com.example.prac1.network.api.FlowerApi
import com.example.prac1.network.requests.RefreshTokenRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserUidRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val tokenRepository: TokenRepository,
    private val api: FlowerApi
) : UserUidRepository {


    private fun getToken(): String? {
        return tokenRepository.getToken()
    }

    private suspend fun refreshToken(): Boolean {
        return try {
            val refreshToken: String = tokenRepository.getRefreshToken()!! //если нет refresh_token выбросим исключение
            val response = runBlocking {
                api.refreshToken(RefreshTokenRequest(refreshToken))
            }
            if (response.isSuccessful) {
                val newAccessToken: String = response.body()?.access_token!! //если в теле не нашли токены выбросим исключение
                val newExpiresIn: Long = response.body()?.expires_in!!
                val newRefreshToken: String = response.body()?.refresh_token!!
                tokenRepository.setToken(newAccessToken, newExpiresIn)
                tokenRepository.setRefreshToken(newRefreshToken)
                true
            } else {
                false //запрос неуспешен
            }
        } catch (e: Exception) {
            false
        }
    }

    // если uid содержится в sharedPref вернем его, иначе попытаемся получить id с сервера
    //вернем null если пользователь не авторизован или отсутствует подключение к интернету/проблемы с сервером
    override suspend fun getUserUid(): String? {
        val uid = sharedPreferences.getString("user_uid", null)
        if (uid == null) {
            Log.d("UID", "shared pref null")
            var newUid:String? = null
                try {
                    //получаем id с сервера
                    var response = api.getUser("Bearer ${getToken()}")
                    if (response.code() == 401 || response.code() == 403) {
                        refreshToken()
                        response = api.getUser("Bearer ${getToken()}")
                    }
                    Log.d("UID", "before issuccess")
                    if(response.isSuccessful) {
                        Log.d("UID", "issuccess")
                        newUid = response.body()?.id!!
                        Log.d("UID", response.body()?.id.toString())
                    } else{
                        //пользователь не авторизован
                        Log.d("UID", "not authorized")
                    }
                } catch (e: Exception) {
                    newUid = null
                    Log.d("UID", e.message.toString())
                    //выводим, что не удается получить доступ к серверу(нет подключения к интернету?)
                }

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
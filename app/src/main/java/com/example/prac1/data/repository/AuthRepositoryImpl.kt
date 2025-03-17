package com.example.prac1.data.repository

import android.util.Log
import com.example.prac1.data.api.FlowerApi
import com.example.prac1.data.api.model.UserInfoDataModel
import com.example.prac1.data.api.requests.LoginRequest
import com.example.prac1.domain.mapper.UserInfoMapper
import com.example.prac1.domain.repository.AuthRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.domain.repository.UserUidRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AuthRepositoryImpl(
    private val api: FlowerApi,
    private val tokenRepository: TokenRepository,
    private val userUidRepository: UserUidRepository
) : AuthRepository {

    private fun prepareFileForUpload(file: File): MultipartBody.Part {
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("file", file.name, requestBody)
    }

    override suspend fun uploadImageToSupabase(imageFile: File, fileName: String): String? {
        val filePart = prepareFileForUpload(imageFile)
        try {
           val response = api.uploadFile(tokenRepository.createAuthHeader(),fileName, filePart)
            if (response.isSuccessful) {
                val key = response.body()?.Key
                val baseUrl = "https://jhivbhezlwbwfhmvxktf.supabase.co"
                return "$baseUrl/storage/v1/object/public/$key"
            } else {
                Log.e("Upload", "Error uploading file: ${response.errorBody()?.string()}")
                return null
            }
        } catch (e: Exception) {
            Log.e("Upload", "Error uploading image", e)
            return null
        }
    }

    override suspend fun uploadUserInfo(name: String, imageUrl: String) {
        val uid = userUidRepository.getUserUid()
        Log.d("UPLOAD", uid.toString())
        uid?.let {
            val userInfo = UserInfoDataModel(
                id = it,
                name = name,
                image_url = imageUrl,
                phone_number = "+79051013134"
            )
            api.addUserInfo(tokenRepository.createAuthHeader(), userInfo)
        }
    }

     override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            val response = api.signIn(LoginRequest(email, password))
            if (response.isSuccessful) {
                val accessToken: String = response.body()?.access_token!!
                val expiresIn: Long = response.body()?.expires_in!!
                val refreshToken: String = response.body()?.refresh_token!!
                tokenRepository.setToken(accessToken, expiresIn)
                tokenRepository.setRefreshToken(refreshToken)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            val response = api.signUp(LoginRequest(email, password))
            if (response.isSuccessful) {
                val result = signIn(email, password)
                result
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun logOut() {
        tokenRepository.clearToken()
        tokenRepository.clearRefreshToken()
    }

    override suspend fun isUserAuthorized(): Boolean {
        if (tokenRepository.getToken() == null && tokenRepository.getRefreshToken() == null) return false
        if (tokenRepository.isTokenExpired()) {
            val successfullyRefreshed = tokenRepository.refreshToken()
            return successfullyRefreshed
        } else {
            return true
        }
    }

}
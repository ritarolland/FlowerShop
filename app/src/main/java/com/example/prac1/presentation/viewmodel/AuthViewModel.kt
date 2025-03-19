package com.example.prac1.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.repository.AuthResult
import com.example.prac1.data.repository.AuthState
import com.example.prac1.domain.repository.AuthRepository
import com.example.prac1.domain.repository.UserUidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepository: AuthRepository,
    private val uidRepository: UserUidRepository) : ViewModel() {
    private val _signInState = MutableStateFlow<AuthState>(AuthState.Default)
    val signInState = _signInState.asStateFlow()

    private val _isAuthorized = MutableStateFlow<Boolean?>(null)
    val isAuthorized = _isAuthorized.asStateFlow()

    init {
        viewModelScope.launch {
            checkAuthorization()
        }
    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri): String {
        val cursor = context.contentResolver.query(contentUri, null, null, null, null)
        cursor?.let {
            it.moveToFirst()
            val idx = it.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = it.getString(idx)
            it.close()
            return path
        }
        return ""
    }

    private suspend fun uploadImage(imageUri: Uri?, context: Context): String? {
        if (imageUri != null) {
            val imageFile = File(getRealPathFromURI(context, imageUri))
            val fileName = "profile_picture_${System.currentTimeMillis()}.jpg"
            if (!imageFile.exists()) {
                return null
            }
            return authRepository.uploadImageToSupabase(imageFile, fileName)
        }
        return null
    }

    fun signIn(email: String, password: String) {
        _signInState.value = AuthState.Loading
        viewModelScope.launch {
            val state = authRepository.signIn(email, password)
            _signInState.value = state
            if (state is AuthState.Success) {
                checkAuthorization()
            }
        }
    }

    fun signUp(email: String, password: String, imageUri: Uri?, context: Context) {
        /*_signInState.value = AuthResult.Loading
        viewModelScope.launch {
            val success = authRepository.signUp(email, password)
            _signInState.value = AuthResult.Success(success)
            checkAuthorization()
            val url = uploadImage(imageUri, context)
            url?.let {
                authRepository.uploadUserInfo(email, it)
            }
        }*/
    }

    private suspend fun checkAuthorization() {
        val authorized = authRepository.isUserAuthorized()
        _isAuthorized.value = authorized
    }

    fun logOut() {
        viewModelScope.launch {
            authRepository.logOut()
            _signInState.value = AuthState.Default
            checkAuthorization()
        }
    }

}
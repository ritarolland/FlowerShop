package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.prac1.presentation.view.AuthResult

class AuthViewModel@Inject constructor(private val authRepository: AuthRepository) : ViewModel(){
    private val _signInState = MutableStateFlow<AuthResult<Boolean>>(AuthResult.Loading)
    val signInState = _signInState.asStateFlow()

    private val _isAuthorized = MutableStateFlow(false)
    val isAuthorized = _isAuthorized.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val success = authRepository.signIn(email, password)
            _signInState.value = AuthResult.Success(success)
        }
    }

    fun checkAuthorization(){
        viewModelScope.launch {
            val authorized =  authRepository.isUserAuthorized()
            _isAuthorized.value = authorized
        }
    }
}
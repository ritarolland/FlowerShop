package com.example.prac1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.data.api.model.UserInfoDataModel
import com.example.prac1.domain.repository.ProfileRepository
import com.example.prac1.domain.repository.UserUidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository,
                                           private val userUidRepository: UserUidRepository) : ViewModel(){
    private val _userInfo = MutableStateFlow<UserInfoDataModel?>(null)
    val userInfo = _userInfo.asStateFlow()

    init {
        loadUserInfo()
    }

    private fun loadUserInfo() {
        viewModelScope.launch {
            profileRepository.getUserInfo().collect { info ->

                Log.d("REPOOOOO","UserInfo in viewModel is $info")
                _userInfo.value = info
            }
        }
    }

    fun fetchProfileUrl() {
        profileRepository.fetchInfo()
    }

    fun logOut() {
        userUidRepository.clearUid()
    }

}
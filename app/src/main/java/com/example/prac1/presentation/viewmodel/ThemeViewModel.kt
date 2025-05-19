package com.example.prac1.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prac1.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemeViewModel @Inject constructor(private val themeRepository: ThemeRepository) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(themeRepository.isDarkTheme())
    val isDarkTheme = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val newTheme = !_isDarkTheme.value
        _isDarkTheme.value = newTheme
        viewModelScope.launch {
            themeRepository.setDarkTheme(newTheme)
        }
    }
}
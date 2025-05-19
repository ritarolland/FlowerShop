package com.example.prac1.data.repository

import android.content.SharedPreferences
import com.example.prac1.domain.repository.ThemeRepository
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences,): ThemeRepository {

    companion object {
        private const val THEME_KEY = "dark_theme_enabled"
    }

    override fun isDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(THEME_KEY, false)
    }

    override fun setDarkTheme(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(THEME_KEY, enabled).apply()
    }
}
package com.example.prac1.data.repository

import androidx.annotation.Keep

@Keep
data class SearchEntry(val query: String, val timestamp: Long)
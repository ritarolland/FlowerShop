package com.example.prac1.data.repository

sealed class SearchResult {
    data object Default : SearchResult()
    data object Loading : SearchResult()
    data object Success : SearchResult()
    data object Error : SearchResult()
}
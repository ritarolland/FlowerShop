package com.example.prac1.presentation.interfaces

import com.example.prac1.domain.model.Flower

interface ItemClickInterface {
    fun onItemClicked(item: Flower)
}
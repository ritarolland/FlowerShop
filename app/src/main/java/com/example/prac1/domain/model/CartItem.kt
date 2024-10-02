package com.example.prac1.domain.model

import androidx.room.Entity
import java.util.UUID

@Entity(tableName = "cart_items")
data class CartItem(
    val id: String,
    val userId: String,
    val flowerId: String,
    var quantity: Int,
)
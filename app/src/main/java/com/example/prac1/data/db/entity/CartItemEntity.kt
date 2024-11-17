package com.example.prac1.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val id: String,
    val flower_id: String,
    val quantity: Int,
    val user_id: String
)

package com.example.prac1.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flower_items")
data class FlowerEntity (
    @PrimaryKey val id: String,
    val name: String,
    val description: String?,
    val price: Double,
    val imageUrl: String
)
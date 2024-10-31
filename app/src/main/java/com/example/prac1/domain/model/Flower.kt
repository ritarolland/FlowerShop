package com.example.prac1.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flowers")
data class Flower(
    @PrimaryKey(autoGenerate = true)
    val id: String = "",
    @ColumnInfo(name = "name")
    val name: String = "Loading...",
    @ColumnInfo(name = "description")
    val description: String = "Description loading...",
    @ColumnInfo(name = "price")
    val price: Double = 0.0,
    @ColumnInfo(name = "image_url")
    val imageUrl: String = ""
)

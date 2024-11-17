package com.example.prac1.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserInfoEntity(
    @PrimaryKey val id: String,
    val name: String,
    val image_url: String,
    val phone_number: String
)

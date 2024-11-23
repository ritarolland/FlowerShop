package com.example.prac1.data.api.model

import java.sql.Timestamp

data class OrderDataModel(
    val id: String,
    val user_id: String,
    val total_price: Double,
    val created_at: Timestamp,
    val address: String
)

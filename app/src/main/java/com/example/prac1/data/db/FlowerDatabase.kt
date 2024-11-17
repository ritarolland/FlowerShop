package com.example.prac1.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.prac1.data.db.entity.FlowerEntity

@Database(entities = [FlowerEntity::class], version = 1)
abstract class FlowerDatabase : RoomDatabase() {
    abstract fun flowerDao(): FlowerDao
}
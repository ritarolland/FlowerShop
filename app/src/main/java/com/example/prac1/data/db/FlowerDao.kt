package com.example.prac1.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.prac1.data.db.entity.FlowerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlowerDao {
    @Query("SELECT * FROM flower_items")
    fun getCachedFlowers(): Flow<List<FlowerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFlowers(flowers: List<FlowerEntity>)
}
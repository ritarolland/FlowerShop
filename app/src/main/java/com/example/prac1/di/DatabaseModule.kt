package com.example.prac1.di

import android.content.Context
import androidx.room.Room
import com.example.prac1.data.db.FlowerDao
import com.example.prac1.data.db.FlowerDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(context: Context): FlowerDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FlowerDatabase::class.java,
            "flower_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideFlowerDao(database: FlowerDatabase): FlowerDao {
        return database.flowerDao()
    }
}
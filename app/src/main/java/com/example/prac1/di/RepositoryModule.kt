package com.example.prac1.di

import com.example.prac1.data.repository.CartRepositoryImpl
import com.example.prac1.data.repository.FlowersRepositoryImpl
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.FlowersRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFlowersRepository(): FlowersRepository {
        return FlowersRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository {
        return CartRepositoryImpl()
    }
}
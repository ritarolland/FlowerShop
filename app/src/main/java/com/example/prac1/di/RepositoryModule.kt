package com.example.prac1.di

import android.content.Context
import android.content.SharedPreferences
import com.example.prac1.data.repository.AuthRepositoryImpl
import com.example.prac1.data.repository.CartRepositoryImpl
import com.example.prac1.data.repository.FlowersRepositoryImpl
import com.example.prac1.data.repository.TokenRepositoryImpl
import com.example.prac1.domain.repository.AuthRepository
import com.example.prac1.domain.repository.CartRepository
import com.example.prac1.domain.repository.FlowersRepository
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.network.api.FlowerApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideFlowersRepository(api: FlowerApi): FlowersRepository {
        return FlowersRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCartRepository(api: FlowerApi, tokenRepository: TokenRepository): CartRepository {
        return CartRepositoryImpl(api, tokenRepository)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(sharedPreferences: SharedPreferences): TokenRepository {
        return TokenRepositoryImpl(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: FlowerApi, tokenRepository: TokenRepository): AuthRepository {
        return AuthRepositoryImpl(api, tokenRepository)
    }
}
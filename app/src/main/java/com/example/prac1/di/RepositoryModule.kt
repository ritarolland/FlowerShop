package com.example.prac1.di

import com.example.prac1.data.repository.AuthRepositoryImpl
import com.example.prac1.data.repository.CartRepositoryImpl
import com.example.prac1.data.repository.FlowersRepositoryImpl
import com.example.prac1.data.repository.TokenRepositoryImpl
import com.example.prac1.domain.auth.TokenProvider
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
    fun provideFlowersRepository(api: FlowerApi): FlowersRepository {
        return FlowersRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCartRepository(api: FlowerApi, tokenProvider: TokenProvider): CartRepository {
        return CartRepositoryImpl(api, tokenProvider)
    }

    @Provides
    @Singleton
    fun provideTokenRepository(): TokenRepository {
        return TokenRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: FlowerApi, tokenRepository: TokenRepository): AuthRepository {
        return AuthRepositoryImpl(api, tokenRepository)
    }
}
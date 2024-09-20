package com.example.prac1.di

import com.example.prac1.data.auth.TokenProviderImpl
import com.example.prac1.domain.auth.TokenProvider
import com.example.prac1.domain.repository.TokenRepository
import com.example.prac1.network.api.FlowerApi
import com.example.prac1.network.interceptors.AuthInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    private val okHttpClientBuilder = OkHttpClient.Builder()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return okHttpClientBuilder
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader(
                        "apikey",
                        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpoaXZiaGV6bHdid2ZobXZ4a3RmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MjY2NTg4MzIsImV4cCI6MjA0MjIzNDgzMn0._g6X4PsH45_sPW0aWfkdJX-2KUgdaILsoQAu_qBDSvQ"
                    )
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    fun addInterceptor(authInterceptor: AuthInterceptor) {
        okHttpClientBuilder.addInterceptor(authInterceptor)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jhivbhezlwbwfhmvxktf.supabase.co")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): FlowerApi {
        return retrofit.create(FlowerApi::class.java)
    }


    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideTokenProvider(
        api: FlowerApi,
        tokenRepository: TokenRepository
    ): TokenProvider {
        return TokenProviderImpl(api, tokenRepository)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenProvider: TokenProvider): AuthInterceptor {
        return AuthInterceptor(tokenProvider)
    }

    @Provides
    @Singleton
    fun provideLazyAuthInterceptor(authInterceptor: AuthInterceptor): Lazy<AuthInterceptor> {
        return lazy { authInterceptor }
    }

    @Provides
    @Singleton
    fun provideLazyTokenProvider(
        tokenProvider: TokenProvider
    ): Lazy<TokenProvider> {
        return lazy { tokenProvider }
    }
}
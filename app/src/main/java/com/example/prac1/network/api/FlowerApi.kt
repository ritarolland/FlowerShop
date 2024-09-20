package com.example.prac1.network.api

import com.example.prac1.data.model.CartItemDataModel
import com.example.prac1.data.model.FlowerDataModel
import com.example.prac1.network.annotations.RequiresAuthorization
import com.example.prac1.network.requests.LoginRequest
import com.example.prac1.network.requests.RefreshTokenRequest
import com.example.prac1.network.responses.LoginResponse
import com.example.prac1.network.responses.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface FlowerApi {

    @GET("/rest/v1/flower_items")
    suspend fun getFlowersCatalog(): Response<List<FlowerDataModel>>

    @GET("/rest/v1/cart_items")
    @RequiresAuthorization
    suspend fun getCartItems(): Response<List<CartItemDataModel>>

    @POST("/rest/v1/cart_items")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @RequiresAuthorization
    suspend fun addCartItem(@Body cartItemDataModel: CartItemDataModel): Response<Any>

    @POST("/auth/v1/token?grant_type=password")
    @Headers("Content-Type: application/json")
    suspend fun signIn(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/auth/v1/token?grant_type=refresh_token")
    @Headers("Content-Type: application/json")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<RefreshTokenResponse>

}
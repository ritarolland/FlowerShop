package com.example.prac1.data.api

import com.example.prac1.data.api.model.CartItemDataModel
import com.example.prac1.data.api.model.FlowerDataModel
import com.example.prac1.data.api.model.UserInfoDataModel
import com.example.prac1.data.api.requests.LoginRequest
import com.example.prac1.data.api.requests.RefreshTokenRequest
import com.example.prac1.data.api.requests.UpdateCartItemRequest
import com.example.prac1.data.api.responses.LoginResponse
import com.example.prac1.data.api.responses.RefreshTokenResponse
import com.example.prac1.data.api.responses.UserIdResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface FlowerApi {

    @GET("/rest/v1/flower_items")
    suspend fun getFlowersCatalog(): Response<List<FlowerDataModel>>

    @GET("/rest/v1/cart_items")
    suspend fun getCartItems(
        @Header("Authorization") token: String
    ): Response<List<CartItemDataModel>>

    @POST("/rest/v1/cart_items")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun addCartItem(
        @Header("Authorization") token: String,
        @Body cartItemDataModel: CartItemDataModel
    ): Response<Unit>

    @POST("/auth/v1/token?grant_type=password")
    @Headers("Content-Type: application/json")
    suspend fun signIn(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/auth/v1/token?grant_type=refresh_token")
    @Headers("Content-Type: application/json")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<RefreshTokenResponse>

    @GET("/auth/v1/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<UserIdResponse>

    @GET("/rest/v1/users")
    suspend fun getUserInfo(
        @Query("id") userId: String
    ): Response<List<UserInfoDataModel>>

    @PATCH("/rest/v1/cart_items")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun updateCartItemQuantity(
        @Query("id") itemId: String,
        @Header("Authorization") token: String,
        @Body updateCartItemRequest: UpdateCartItemRequest
    ): Response<Unit>

}
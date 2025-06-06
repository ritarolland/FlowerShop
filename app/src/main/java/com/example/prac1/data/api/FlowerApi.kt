package com.example.prac1.data.api

import com.example.prac1.data.api.model.CartItemDataModel
import com.example.prac1.data.api.model.FavouriteDataModel
import com.example.prac1.data.api.model.FlowerDataModel
import com.example.prac1.data.api.model.OrderDataModel
import com.example.prac1.data.api.model.UserInfoDataModel
import com.example.prac1.data.api.requests.LoginRequest
import com.example.prac1.data.api.requests.OrderCartItemRequest
import com.example.prac1.data.api.requests.RefreshTokenRequest
import com.example.prac1.data.api.requests.UpdateCartItemRequest
import com.example.prac1.data.api.responses.ImageUploadResponse
import com.example.prac1.data.api.responses.LoginResponse
import com.example.prac1.data.api.responses.RefreshTokenResponse
import com.example.prac1.data.api.responses.UserIdResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface FlowerApi {

    @GET("/rest/v1/flower_items")
    suspend fun getFlowersCatalog(): Response<List<FlowerDataModel>>

    @GET("/rest/v1/flower_items")
    suspend fun search(
        @Query("name") name: String
    ): Response<List<FlowerDataModel>>

    @GET("/rest/v1/cart_items?order_id=is.null")
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

    @POST("/auth/v1/signup")
    @Headers("Content-Type: application/json")
    suspend fun signUp(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("/auth/v1/token?grant_type=refresh_token")
    @Headers("Content-Type: application/json")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): Response<RefreshTokenResponse>

    @GET("/auth/v1/user")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<UserIdResponse>

    @POST("/rest/v1/users")
    suspend fun addUser(
        @Header("Authorization") token: String,
        @Body user: UserInfoDataModel
    ): Response<Unit>

    @GET("/rest/v1/users")
    suspend fun getUserInfo(
        @Query("id") userId: String
    ): Response<List<UserInfoDataModel>>

    @GET("/rest/v1/favourites")
    suspend fun loadFavourites(
        @Header("Authorization") token: String,
        @Query("user_id") userId: String
    ): Response<List<FavouriteDataModel>>

    @GET("/rest/v1/orders")
    suspend fun getOrders(
        @Header("Authorization") token: String,
        @Query("user_id") userId: String
    ): Response<List<OrderDataModel>>

    @GET("/rest/v1/orders")
    suspend fun getOrderById(
        @Header("Authorization") token: String,
        @Query("id") orderId: String
    ): Response<List<OrderDataModel>>

    @GET("rest/v1/cart_items")
    suspend fun getOrderItems(
        @Header("Authorization") token: String,
        @Query("order_id") orderId: String
    ): Response<List<CartItemDataModel>>

    @POST("/rest/v1/favourites")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun addFavourite(
        @Header("Authorization") token: String,
        @Body favouriteDataModel: FavouriteDataModel
    ): Response<Unit>

    @DELETE("/rest/v1/favourites")
    suspend fun deleteFavourite(
        @Header("Authorization") token: String,
        @Query("id") id: String
    ): Response<Unit>

    @GET("/rest/v1/flower_items")
    suspend fun getFlowerById(
        @Header("Authorization") token: String,
        @Query("id") flowerId: String
    ): Response<List<FlowerDataModel>>

    @DELETE("/rest/v1/cart_items")
    suspend fun deleteItemFromCart(
        @Header("Authorization") token: String,
        @Query("id") itemId: String
    ): Response<Unit>

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

    @PATCH("/rest/v1/cart_items")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun orderCartItem(
        @Query("id") itemId: String,
        @Header("Authorization") token: String,
        @Body orderCartItemRequest: OrderCartItemRequest
    ): Response<Unit>

    @POST("/rest/v1/orders")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun addOrder(
        @Header("Authorization") token: String,
        @Body orderDataModel: OrderDataModel
    ) : Response<Unit>

    @Multipart
    @POST("storage/v1/object/userImages/{file_name}")
    suspend fun uploadFile(
        @Header("Authorization") token: String,
        @Path("file_name") fileName: String,
        @Part file: MultipartBody.Part
    ): Response<ImageUploadResponse>

    @POST("/rest/v1/users")
    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    suspend fun addUserInfo(
        @Header("Authorization") token: String,
        @Body userInfoDataModel: UserInfoDataModel
    ) : Response<Unit>

}
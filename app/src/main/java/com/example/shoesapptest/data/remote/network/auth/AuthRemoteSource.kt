package com.example.shoesapptest.data.remote.network.auth

import com.example.shoesapptest.data.remote.network.AuthRequest
import com.example.shoesapptest.data.remote.network.AuthResponse
import com.example.shoesapptest.data.remote.network.PopularResponse
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.data.remote.network.RegistrationRequest
import com.example.shoesapptest.data.remote.network.RegistrationResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthRemoteSource {

    @POST("/login")
    suspend fun login(@Body authorizationRequest: AuthRequest): AuthResponse

    @POST("/registration")
    suspend fun  registration(@Body registrationRequest: RegistrationRequest): RegistrationResponse

    @GET("/allSneakers")
    suspend fun popular(): List<PopularResponse>

    @GET("/sneakers/{category}")
    suspend fun getSneakersByCategory(@Path("category") category: String): List<PopularResponse>

    @GET("/sneakers/popular")
    suspend fun getPopularSneakers(): List<PopularResponse>

    @GET("/favorites")
    suspend fun getFavorites(): List<PopularResponse>

    @POST("favorites/{sneakerId}")
    suspend fun addToFavorites(@Path("sneakerId") sneakerId: Int): Response<Unit>

    @DELETE("favorites/{sneakerId}")
    suspend fun removeFromFavorites(@Path("sneakerId") sneakerId: Int): Response<Unit>

    @GET("/cart")
    suspend fun getCart(): List<PopularResponse>

    @POST("/cart/{sneakerId}")
    suspend fun addToCart(@Path("sneakerId") sneakerId: Int): Response<Unit>

    @DELETE("/cart/{sneakerId}")
    suspend fun removeFromCart(@Path("sneakerId") sneakerId: Int): Response<Unit>

    @DELETE("cart/remove-all/{sneakerId}")
    suspend fun removeAllFromCart(@Path("sneakerId") sneakerId: Int): Response<Unit>

    @GET("/cart/total")
    suspend fun getCartTotal(): Map<String, Double>

    @PUT("/cart/update-quantity")
    suspend fun updateCartQuantity(
        @Query("productId") productId: Int,
        @Query("quantity") quantity: Int
    ): Response<Unit>

    @GET("/sneakers/search")
    suspend fun searchSneakers(@Query("query") query: String): List<PopularResponse>


}




package com.example.shoesapptest.data.repository

import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.data.remote.network.auth.AuthRemoteSource
import com.example.shoesapptest.data.remote.network.AuthRequest
import com.example.shoesapptest.data.remote.network.AuthResponse
import com.example.shoesapptest.data.remote.network.CartTotal
import com.example.shoesapptest.data.remote.network.NetworkResponse
import com.example.shoesapptest.data.remote.network.PopularResponse
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.data.remote.network.RegistrationRequest
import com.example.shoesapptest.data.remote.network.RegistrationResponse

class AuthRepository(val authRemoteSource: AuthRemoteSource) {

    suspend fun authorization(authorizationRequest: AuthRequest): AuthResponse {
        return authRemoteSource.login(authorizationRequest)
    }

    suspend fun registration(registrationRequest: RegistrationRequest): RegistrationResponse {
        return authRemoteSource.registration(registrationRequest)
    }

    suspend fun getSneakers(): NetworkResponseSneakers<List<PopularResponse>> {
        return try {
            val result = authRemoteSource.popular()
            NetworkResponseSneakers.Success(result)
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun getPopularSneakers(): NetworkResponseSneakers<List<PopularResponse>> {
        return try {
            val result = authRemoteSource.getPopularSneakers()
            NetworkResponseSneakers.Success(result)
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun getFavorites(): NetworkResponseSneakers<List<PopularResponse>> {
        return try {
            val result = authRemoteSource.getFavorites()
            NetworkResponseSneakers.Success(result)
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun toggleFavorite(sneakerId: Int, isFavorite: Boolean): NetworkResponse<Unit> {
        return try {
            if (isFavorite) {
                authRemoteSource.addToFavorites(sneakerId)
            } else {
                authRemoteSource.removeFromFavorites(sneakerId)
            }
            NetworkResponse.Success(Unit)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Failed to toggle favorite")
        }
    }

    suspend fun getSneakersByCategory(category: String): NetworkResponseSneakers<List<PopularResponse>> {
        return try {
            val result = authRemoteSource.getSneakersByCategory(category)
            NetworkResponseSneakers.Success(result)
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Unknown Error")
        }
    }

    suspend fun addToFavorites(sneakerId: Int): NetworkResponse<Unit> {
        return try {
            authRemoteSource.addToFavorites(sneakerId)
            NetworkResponse.Success(Unit)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Failed to add to favorites")
        }
    }

    suspend fun removeFromFavorites(sneakerId: Int): NetworkResponse<Unit> {
        return try {
            authRemoteSource.removeFromFavorites(sneakerId)
            NetworkResponse.Success(Unit)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Failed to remove from favorites")
        }
    }

    suspend fun getCart(): NetworkResponseSneakers<List<PopularResponse>> {
        return try {
            val result = authRemoteSource.getCart()
            NetworkResponseSneakers.Success(result)
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Failed to get cart")
        }
    }

    suspend fun getCartTotal(): NetworkResponse<CartTotal> {
        return try {
            val result = authRemoteSource.getCartTotal()
            NetworkResponse.Success(
                CartTotal(
                    itemsCount = result["itemsCount"]?.toInt() ?: 0,
                    total = result["total"] ?: 0.0,
                    delivery = result["delivery"] ?: 0.0,
                    finalTotal = result["finalTotal"] ?: 0.0
                )
            )
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Failed to get cart total")
        }
    }

    suspend fun addToCart(sneakerId: Int): NetworkResponse<Unit> {
        return try {
            authRemoteSource.addToCart(sneakerId)
            NetworkResponse.Success(Unit)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Failed to add to cart")
        }
    }

    suspend fun removeFromCart(sneakerId: Int): NetworkResponse<Unit> {
        return try {
            authRemoteSource.removeFromCart(sneakerId)
            NetworkResponse.Success(Unit)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Failed to remove from cart")
        }
    }

    suspend fun updateCartQuantity(productId: Int, quantity: Int): NetworkResponse<Unit> {
        return try {
            authRemoteSource.updateCartQuantity(productId, quantity)
            NetworkResponse.Success(Unit)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Failed to update quantity")
        }
    }

    suspend fun removeAllFromCart(sneakerId: Int): NetworkResponse<Unit> {
        return try {
            authRemoteSource.removeAllFromCart(sneakerId)
            NetworkResponse.Success(Unit)
        } catch (e: Exception) {
            NetworkResponse.Error(e.message ?: "Failed to remove all from cart")
        }
    }

    suspend fun searchSneakers(query: String): NetworkResponseSneakers<List<PopularResponse>> {
        return try {
            val result = authRemoteSource.searchSneakers(query)
            NetworkResponseSneakers.Success(result)
        } catch (e: Exception) {
            NetworkResponseSneakers.Error(e.message ?: "Search failed")
        }
    }
}
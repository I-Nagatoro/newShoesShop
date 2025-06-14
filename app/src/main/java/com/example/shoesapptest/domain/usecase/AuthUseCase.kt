package com.example.shoesapptest.domain.usecase

import com.example.shoesapptest.data.local.DataStore
import com.example.shoesapptest.data.remote.network.NetworkResponse
import com.example.shoesapptest.data.remote.network.NetworkResponseSneakers
import com.example.shoesapptest.data.remote.network.AuthRequest
import com.example.shoesapptest.data.remote.network.AuthResponse
import com.example.shoesapptest.data.remote.network.CartTotal
import com.example.shoesapptest.data.remote.network.PopularResponse
import com.example.shoesapptest.data.remote.network.SneakersResponse
import com.example.shoesapptest.data.remote.network.RegistrationRequest
import com.example.shoesapptest.data.remote.network.RegistrationResponse
import com.example.shoesapptest.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class AuthUseCase(private val dataStore: DataStore, private val authRepository: AuthRepository) {
    val token: Flow<String> = dataStore.tokenFlow

    fun registration(registrationRequest: RegistrationRequest): Flow<NetworkResponse<RegistrationResponse>> = flow {
        try {
            emit(NetworkResponse.Loading)
            if (!EmailValidator().validate(registrationRequest.email)) {
                emit(NetworkResponse.Error("Invalid email format"))
                return@flow
            }
            if (!PasswordValidator().validate(registrationRequest.password)) {
                emit(NetworkResponse.Error("Password must contain: 8+ chars, 1 uppercase, 1 digit, 1 special char"))
                return@flow
            }

            val result = authRepository.registration(registrationRequest)
            dataStore.setToken(result.token)
            emit(NetworkResponse.Success(result))
        } catch (e: Exception) {
            emit(NetworkResponse.Error(e.message ?: "Unknown Error"))

            println("Registration failed: ${e.message}")
        }
    }

    fun authorization(authorizationRequest: AuthRequest): Flow<NetworkResponse<AuthResponse>> = flow {
        try {
            emit(NetworkResponse.Loading)
            val result = authRepository.authorization(authorizationRequest)
            dataStore.setToken(result.token)
            emit(NetworkResponse.Success(result))
        } catch (e: Exception) {
            emit(NetworkResponse.Error(e.message ?: "Unknown Error"))
        }
    }

    suspend fun getSneakersByCategory(category: String): NetworkResponseSneakers<List<PopularResponse>> {
        return authRepository.getSneakersByCategory(category)
    }

    suspend fun getPopularSneakers(): NetworkResponseSneakers<List<PopularResponse>> {
        return authRepository.getPopularSneakers()
    }

    suspend fun getFavorites(): NetworkResponseSneakers<List<PopularResponse>> {
        return authRepository.getFavorites()
    }

    suspend fun toggleFavorite(sneakerId: Int, isFavorite: Boolean): NetworkResponse<Unit> {
        return authRepository.toggleFavorite(sneakerId, isFavorite)
    }

    suspend fun addToFavorites(sneakerId: Int): NetworkResponse<Unit> {
        return authRepository.addToFavorites(sneakerId)
    }

    suspend fun removeFromFavorites(sneakerId: Int): NetworkResponse<Unit> {
        return authRepository.removeFromFavorites(sneakerId)
    }

    suspend fun getCart(): NetworkResponseSneakers<List<PopularResponse>> {
        return authRepository.getCart()
    }

    suspend fun getCartTotal(): NetworkResponse<CartTotal> {
        return authRepository.getCartTotal()
    }

    suspend fun addToCart(sneakerId: Int): NetworkResponse<Unit> {
        return authRepository.addToCart(sneakerId)
    }

    suspend fun removeFromCart(sneakerId: Int): NetworkResponse<Unit> {
        return authRepository.removeFromCart(sneakerId)
    }

    suspend fun removeAllFromCart(sneakerId: Int): NetworkResponse<Unit> {
        return authRepository.removeAllFromCart(sneakerId)
    }

    suspend fun updateCartQuantity(productId: Int, quantity: Int): NetworkResponse<Unit> {
        return authRepository.updateCartQuantity(productId, quantity)
    }

    suspend fun searchSneakers(query: String): NetworkResponseSneakers<List<PopularResponse>> {
        return authRepository.searchSneakers(query)
    }
}
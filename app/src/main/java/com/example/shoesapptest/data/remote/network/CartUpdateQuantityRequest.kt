package com.example.shoesapptest.data.remote.network

import kotlinx.serialization.Serializable

@Serializable
data class CartUpdateQuantityRequest(
    val productId: Int,
    val quantity: Int
)
package com.example.shoesapptest.data.remote.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SneakersInCart(
    val sneaker: PopularResponse,
    var quantity: Int
)
package com.example.shoesapptest.data.remote.network

import kotlinx.serialization.Serializable

@Serializable
data class CartTotal(
    val itemsCount: Int,
    val total: Double,
    val delivery: Double,
    val finalTotal: Double
)
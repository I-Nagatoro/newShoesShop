package com.example.shoesapptest.data.remote.network

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequest(
    val sneakerId: Int
)
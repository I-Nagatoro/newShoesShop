package com.example.shoesapptest.data.remote.network

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val message: String,
    val errorCode: Int
)
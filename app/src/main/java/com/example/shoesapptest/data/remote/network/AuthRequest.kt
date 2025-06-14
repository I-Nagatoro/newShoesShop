package com.example.shoesapptest.data.remote.network

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest (
    val email: String,
    val password: String
    )
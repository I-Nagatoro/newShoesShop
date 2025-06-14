package com.example.shoesapptest.domain.usecase

interface Validator {
    fun <T> validate(value: T ): Boolean
}
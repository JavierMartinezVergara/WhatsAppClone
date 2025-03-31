package com.example.domain

interface IInternalTokenRepository {
    suspend fun storeToken(userId: String, token: String)
}
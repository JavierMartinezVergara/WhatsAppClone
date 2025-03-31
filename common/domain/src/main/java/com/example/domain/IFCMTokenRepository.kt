package com.example.domain

interface IFCMTokenRepository {
    suspend fun getFCMToken(): String?
}
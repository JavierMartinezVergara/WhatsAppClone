package com.example.data

import com.example.domain.IInternalTokenRepository

class InternalTokenRepository(): IInternalTokenRepository {
    override suspend fun storeToken(userId: String, token: String) {
        TODO("Not yet implemented")
    }
}
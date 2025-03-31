package com.example.data

import com.example.domain.FCMTokenDataSource
import com.example.domain.IFCMTokenRepository
import javax.inject.Inject

class FCMTokenRepository @Inject constructor(private val tokenDataSource: FCMTokenDataSource):
    IFCMTokenRepository {
    override suspend fun getFCMToken(): String? {
        return tokenDataSource.getFcmToken()
    }



}
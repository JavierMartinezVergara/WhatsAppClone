package com.example.chat.domain

import com.example.chat.domain.model.DomainMessage
import kotlinx.coroutines.flow.Flow

interface IMessageRepository {

    suspend fun getMessages(): Flow<DomainMessage>
    suspend fun sendMessage(domainMessage: DomainMessage)
    suspend fun disconnect()
}
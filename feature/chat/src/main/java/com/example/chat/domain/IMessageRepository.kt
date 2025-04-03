package com.example.chat.domain

import com.example.chat.domain.model.DomainMessage
import kotlinx.coroutines.flow.Flow

interface IMessageRepository {

    suspend fun getMessages(chatId: String, userId: String): Flow<DomainMessage>
    suspend fun sendMessage(chatId: String, domainMessage: DomainMessage)
    suspend fun disconnect()
}
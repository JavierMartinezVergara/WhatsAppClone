package com.example.chat.data.network.repository

import com.example.chat.data.network.datasource.FirestoreMessagesDataStore
import com.example.chat.data.network.datasource.MessageSocketDataSource
import com.example.chat.domain.IMessageRepository
import com.example.chat.domain.model.DomainMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepository @Inject constructor(
    // private val dataSource: MessageSocketDataSource
    private val dataSource: FirestoreMessagesDataStore
) : IMessageRepository {

    override suspend fun getMessages(chatId: String, userId: String
    ): Flow<DomainMessage> {
        return dataSource.getMessages(chatId, userId)
    }

    override suspend fun sendMessage(chatId: String, domainMessage: DomainMessage) {
        dataSource.sendMessage(chatId, domainMessage)
    }

    override suspend fun disconnect() {
        // dataSource.disconnect()
    }
}
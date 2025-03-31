package com.example.chat.data.network.repository

import com.example.chat.data.network.datasource.MessageSocketDataSource
import com.example.chat.domain.IMessageRepository
import com.example.chat.domain.model.DomainMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val dataSource: MessageSocketDataSource
): IMessageRepository{

    override suspend fun getMessages(): Flow<DomainMessage> {
        return dataSource.connect()
    }

    override suspend fun sendMessage(domainMessage: DomainMessage) {
        dataSource.sendMessage(domainMessage)
    }

    override suspend fun disconnect() {
        dataSource.disconnect()
    }
}
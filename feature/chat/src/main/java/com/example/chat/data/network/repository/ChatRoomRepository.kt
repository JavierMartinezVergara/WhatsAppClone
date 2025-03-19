package com.example.chat.data.network.repository

import com.example.chat.data.network.datasource.ChatRoomDataSource
import com.example.chat.domain.IChatRoomRepository
import com.example.chat.domain.model.ChatRoom
import javax.inject.Inject

class ChatRoomRepository @Inject constructor(
    private val chatDataSoource: ChatRoomDataSource
) : IChatRoomRepository {

    override suspend fun getInitialChatRoom(id: String): ChatRoom {
        val chatRooApiModel = chatDataSoource.getInitialChatRoom(id)
        return chatRooApiModel.toDomain()
    }
}
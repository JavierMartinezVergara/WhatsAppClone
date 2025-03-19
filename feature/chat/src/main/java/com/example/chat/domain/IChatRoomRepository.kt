package com.example.chat.domain

import com.example.chat.domain.model.ChatRoom

interface IChatRoomRepository {

    suspend fun getInitialChatRoom(id: String): ChatRoom
}
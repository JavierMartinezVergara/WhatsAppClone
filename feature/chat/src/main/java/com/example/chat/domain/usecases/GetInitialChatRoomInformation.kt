package com.example.chat.domain.usecases

import com.example.chat.data.network.repository.ChatRoomRepository
import com.example.chat.domain.IChatRoomRepository
import com.example.chat.domain.model.ChatRoom
import javax.inject.Inject

class GetInitialChatRoomInformation @Inject constructor(
    private val chatRoomRepository: IChatRoomRepository
) {
    suspend operator fun invoke(id: String): ChatRoom {
        return chatRoomRepository.getInitialChatRoom(id)
    }
}
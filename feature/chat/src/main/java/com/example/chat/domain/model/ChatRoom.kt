package com.example.chat.domain.model

data class ChatRoom(
    val id: String,
    val senderName: String,
    val senderAvatar: String,
    val lastMessages: List<DomainMessage>
)
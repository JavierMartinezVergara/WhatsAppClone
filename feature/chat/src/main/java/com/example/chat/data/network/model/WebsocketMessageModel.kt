package com.example.chat.data.network.model

import com.example.chat.domain.model.DomainMessage
import kotlinx.serialization.Serializable

@Serializable
class WebsocketMessageModel(
    val id: String? = null,
    val message: String,
    val senderName: String,
    val senderAvatar: String,
    val timestamp: String? = null,
    val isMine: Boolean,
    val messageType: String,
    val messageDescription: String
) {

    companion object {
        const val TYPE_TEXT = "TEXT"
        const val TYPE_IMAGE = "IMAGE"

        fun fromDomain(domainMessage: DomainMessage): WebsocketMessageModel {
            return WebsocketMessageModel(
                message = domainMessage.content,
                senderAvatar = domainMessage.senderAvatar,
                senderName = domainMessage.senderName,
                isMine = domainMessage.isMine,
                messageType = domainMessage.fromContentType(),
                messageDescription = domainMessage.contentDescription
            )
        }
    }

    fun toDomain(): DomainMessage {
        return DomainMessage(
            id = id,
            content = message,
            senderAvatar = senderAvatar,
            senderName = senderName,
            timestamp = timestamp,
            isMine = isMine,
            contentDescription = messageDescription,
            contentType = toContentType()
        )
    }

    fun toContentType(): DomainMessage.ContentType {
        return when(messageType) {
            TYPE_IMAGE -> DomainMessage.ContentType.IMAGE
            else -> DomainMessage.ContentType.TEXT
        }
    }
}

fun DomainMessage.fromContentType(): String {
    return when(contentType) {
        DomainMessage.ContentType.IMAGE -> WebsocketMessageModel.TYPE_IMAGE
        else -> WebsocketMessageModel.TYPE_TEXT
    }
}
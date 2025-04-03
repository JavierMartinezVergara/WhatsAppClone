package com.example.chat.data.network.model

import com.example.chat.domain.model.DomainMessage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.text.SimpleDateFormat
import java.util.Locale

data class FirestoreMessageModel(
    @Transient
    val id: String = "",

    @get:PropertyName("senderId")
    @set:PropertyName("senderId")
    var senderId: String = "",

    @get:PropertyName("senderName")
    @set:PropertyName("senderName")
    var senderName: String = "",

    @get:PropertyName("senderAvatar")
    @set:PropertyName("senderAvatar")
    var senderAvatar: String = "",

    @get:PropertyName("content")
    @set:PropertyName("content")
    var content: String = "",

    @get:PropertyName("timestamp")
    @set:PropertyName("timestamp")
    var timestamp: Timestamp = Timestamp.now()
) {
    companion object {
        fun fromDomain(message: DomainMessage): FirestoreMessageModel {
            return FirestoreMessageModel(
                id = "",
                senderName = message.senderName,
                senderAvatar = message.senderAvatar,
                content = message.content
            )
        }
    }

    fun toDomain(userId: String): DomainMessage {
        return DomainMessage(
            id = id,
            senderName = senderName,
            senderAvatar = senderAvatar,
            isMine = userId == senderId,
            contentType = DomainMessage.ContentType.TEXT,
            content = content,
            contentDescription = "",
            timestamp = timestamp.toDateString()
        )
    }

    private fun Timestamp.toDateString(): String {
        // Create a SimpleDateFormat instance with the desired format and the default Locale
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

        // Convert the Timestamp to a Date object
        val date = toDate()

        // Format the Date object using the SimpleDateFormat instance
        return formatter.format(date)
    }
}
package com.example.chat.data.network.datasource

import com.example.chat.data.network.model.FirestoreMessageModel
import com.example.chat.domain.model.DomainMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirestoreMessagesDataStore @Inject constructor(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    companion object {
        const val MESSAGES = "messages"
        const val CHATS = "chats"
        const val TIMESTAMP = "timestamp"
    }

    fun getMessages(chatId: String, userId: String): Flow<DomainMessage> {
        return callbackFlow {
            val messagesCollection =
                firestore.collection(CHATS).document(chatId).collection(MESSAGES)
            val query = messagesCollection.orderBy(TIMESTAMP, Query.Direction.ASCENDING)
            val listenerRegistration = query.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                } else {
                    val messages = snapshot?.documents?.mapNotNull { doc ->
                        val message = doc.toObject(FirestoreMessageModel::class.java)
                        message?.copy(id = doc.id)
                    } ?: emptyList()
                    val domainMessages = messages.map { it.toDomain(userId = userId) }
                    try {
                        domainMessages.forEach {
                            trySend(it)
                        }
                    } catch (e: Exception) {
                        close(e)
                    }
                }
            }
            awaitClose { listenerRegistration.remove() }
        }
    }

    fun sendMessage(chatId: String, message: DomainMessage) {
        val chatRef =
            firestore.collection("chats").document(chatId)
                .collection("messages")
        chatRef.add(FirestoreMessageModel
            .fromDomain(message))
    }
}
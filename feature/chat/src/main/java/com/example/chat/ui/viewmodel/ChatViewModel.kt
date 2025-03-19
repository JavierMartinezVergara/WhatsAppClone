package com.example.chat.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chat.domain.model.ChatRoom
import com.example.chat.domain.model.DomainMessage
import com.example.chat.domain.usecases.DisconnectMessages
import com.example.chat.domain.usecases.GetInitialChatRoomInformation
import com.example.chat.domain.usecases.RetrieveMessage
import com.example.chat.domain.usecases.SendMessage
import com.example.chat.ui.model.Chat
import com.example.chat.ui.model.Message
import com.example.chat.ui.model.MessageContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val retrieveMessage: RetrieveMessage,
    private val sendMessage: SendMessage,
    private val disconnectMessages: DisconnectMessages,
    private val getInitialChatRoomInformation: GetInitialChatRoomInformation
) : ViewModel() {

    private val _uiState = MutableStateFlow(Chat())
    val uiState: StateFlow<Chat> = _uiState

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages

    private var messageCollectionJob: Job? = null

    fun loadChatInformation(id: String) {
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            val chatRoom = getInitialChatRoomInformation(id)
            withContext(Dispatchers.Main) {
                _uiState.value = chatRoom.toUI()
                _messages.value = chatRoom.lastMessages.map {
                    it.toUI()
                }
                updateMessages()
            }
        }
    }

    fun updateMessages() {
        messageCollectionJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                retrieveMessage()
                    .map { it.toUI() }
                    .collect { message ->
                        withContext(Dispatchers.Main) {
                            _messages.value += message
                        }
                    }
            } catch (ie: Throwable) {
                Log.d(
                    "TODO",
                    "You can show here a message to the user indicating that an error has happened"
                )
            }
        }
    }

    fun onSendMessage(messageText: String) {
        viewModelScope.launch {
            val message = DomainMessage(
                senderAvatar = "user.avatar",
                senderName = "user.name",
                isMine = true,
                contentType = DomainMessage.ContentType.TEXT,
                content = messageText,
                contentDescription = messageText
            )
            sendMessage(message)
        }
    }

    private fun DomainMessage.toUI(): Message {
        return Message(
            id = id ?: "",
            senderName = senderName,
            senderAvatar = senderAvatar,
            timestamp = timestamp ?: "",
            isMine = isMine,
            messageContent = getMessageContent()
        )
    }

    private fun DomainMessage.getMessageContent():
            MessageContent {
        return when (contentType) {
            DomainMessage.ContentType.TEXT ->
                MessageContent.TextMessage(content)

            DomainMessage.ContentType.IMAGE ->
                MessageContent.ImageMessage(
                    content,
                    contentDescription
                )
        }
    }

    private fun ChatRoom.toUI(): Chat {
        return Chat(
            id = id,
            name = senderName,
            avatar = senderAvatar
        )
    }

    override fun onCleared() {
        super.onCleared()
        messageCollectionJob?.cancel()
        viewModelScope.launch(Dispatchers.IO) {
            disconnectMessages()
        }
    }
}
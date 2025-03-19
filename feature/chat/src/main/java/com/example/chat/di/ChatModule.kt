package com.example.chat.di

import com.example.chat.data.network.RestClient
import com.example.chat.data.network.WebSocketClient
import com.example.chat.data.network.repository.ChatRoomRepository
import com.example.chat.data.network.repository.MessageRepository
import com.example.chat.domain.IChatRoomRepository
import com.example.chat.domain.IMessageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
abstract class ChatModule {

    companion object {
        const val WEBSOCKET_URL =
            "ws://whatspackt.com/chat/%s"
        const val WEBSOCKET_URL_NAME = "WEBSOCKET_URL"
        const val WEBSOCKET_CLIENT = "WEBSOCKET_CLIENT"
        const val API_CHAT_ROOM_URL =
            "http://whatspackt.com/chats/%s"
        const val API_CHAT_ROOM_URL_NAME = "CHATROOM_URL"
        const val API_CLIENT = "API_CLIENT"
    }

    @Provides
    @Named(WEBSOCKET_CLIENT)
    fun providesWebsocketHttpClient(): HttpClient {
        return WebSocketClient.client
    }

    @Provides
    @Named(WEBSOCKET_URL_NAME)
    fun providesWebsocketURL(): String {
        return WEBSOCKET_URL
    }

    @Provides
    @Named(API_CLIENT)
    fun providesAPIHttpClient(): HttpClient {
        return RestClient.client
    }
}

@InstallIn(SingletonComponent::class)
@Module
abstract class ChatBindingsModule {
    @Binds
    abstract fun providesChatRoomRepository(
        chatRoomRepository: ChatRoomRepository
    ): IChatRoomRepository

    @Binds
    abstract fun providesMessagesRepository(
        messagesRepository: MessageRepository
    ): IMessageRepository
}
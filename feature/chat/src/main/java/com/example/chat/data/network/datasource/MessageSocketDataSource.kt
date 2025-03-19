package com.example.chat.data.network.datasource

import com.example.chat.data.network.model.WebsocketMessageModel
import com.example.chat.di.ChatModule.Companion.WEBSOCKET_CLIENT
import com.example.chat.di.ChatModule.Companion.WEBSOCKET_URL_NAME
import com.example.chat.domain.model.DomainMessage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.converter
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.serialization.deserialize
import io.ktor.serialization.serialize
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Named

class MessageSocketDataSource @Inject constructor(
    @Named(WEBSOCKET_CLIENT) private val httpClient: HttpClient,
    @Named(WEBSOCKET_URL_NAME) private val websocketUrl: String
) {
    private lateinit var webSocketSession: DefaultClientWebSocketSession

    suspend fun connect(): Flow<DomainMessage> {
        return httpClient.webSocketSession {
            url(websocketUrl)
        }.apply { webSocketSession = this }
            .incoming
            .receiveAsFlow()
            .map { frame ->
                webSocketSession.handleMessage(frame)
            }
            .filterNotNull()
            .map {
                it.toDomain()
            }
    }

    suspend fun sendMessage(domainMessage: DomainMessage) {
        val websocketMessage = WebsocketMessageModel.fromDomain(domainMessage)
        webSocketSession.converter?.serialize(websocketMessage)?.let {
            webSocketSession.send(it)
        }
    }

    suspend fun disconnect() {
        webSocketSession.close(
            CloseReason(
                CloseReason.Codes.NORMAL, "Disconnect"
            )
        )
    }

    private suspend fun DefaultClientWebSocketSession.handleMessage(frame: Frame): WebsocketMessageModel? {
        return when (frame) {
            is Frame.Text -> converter?.deserialize(frame)
            is Frame.Close -> {
                disconnect()
                null
            }

            else -> null
        }

    }
}


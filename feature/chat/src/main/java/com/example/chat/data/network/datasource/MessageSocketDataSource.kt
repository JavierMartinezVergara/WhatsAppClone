package com.example.chat.data.network.datasource

import android.util.Log
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import javax.inject.Inject
import javax.inject.Named

class MessageSocketDataSource @Inject constructor(
    @Named(WEBSOCKET_CLIENT) private val httpClient: HttpClient,
    @Named(WEBSOCKET_URL_NAME) private val websocketUrl: String
) {

    companion object {
        const val TAG = "MessagesSocketDataSource"
        const val RETRY_DELAY = 30000L
        const val MAX_RETRIES = 5
    }

    private lateinit var webSocketSession: DefaultClientWebSocketSession

    suspend fun connect(): Flow<DomainMessage> {
        return flow {
            try {
                httpClient.webSocketSession {
                    url(websocketUrl)
                }.apply { webSocketSession = this }
                    .incoming
                    .receiveAsFlow()
                    .collect { frame ->
                        try {
                            val message = webSocketSession.handleMessage(frame)
                                ?.toDomain()
                            if (message != null) {
                                emit(message)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error handling WebSocket frame", e)
                        }

                    }
            } catch (e: Exception) {
                Log.e(TAG, "Error connecting to WebSocket", e)
            }
        }.retryWhen { cause, attempt ->
            if (cause is IOException && attempt < MAX_RETRIES) {
                delay(RETRY_DELAY)
                true
            } else {
                false
            }
        }.catch { e ->
            Log.e(TAG, "Error in WebSocket Flow", e)
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


package com.example.chat.domain.usecases

import com.example.chat.domain.IMessageRepository
import javax.inject.Inject

class DisconnectMessages @Inject constructor(
    private val repository: IMessageRepository
) {

    suspend operator fun invoke() {
        repository.disconnect()
    }
}
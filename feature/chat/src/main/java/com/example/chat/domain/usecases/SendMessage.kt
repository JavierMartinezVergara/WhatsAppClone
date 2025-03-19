package com.example.chat.domain.usecases

import com.example.chat.domain.IMessageRepository
import com.example.chat.domain.model.DomainMessage
import javax.inject.Inject

class SendMessage @Inject constructor(
    private val repository: IMessageRepository
) {
    suspend operator fun invoke(domainMessage: DomainMessage) {
        repository.sendMessage(domainMessage)
    }
}
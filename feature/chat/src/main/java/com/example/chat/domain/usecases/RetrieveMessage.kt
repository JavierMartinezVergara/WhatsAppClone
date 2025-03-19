package com.example.chat.domain.usecases

import com.example.chat.domain.IMessageRepository
import com.example.chat.domain.model.DomainMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RetrieveMessage @Inject constructor(
    private val repository: IMessageRepository
) {
    suspend operator fun invoke(): Flow<DomainMessage> {
        return repository.getMessages()
    }
}
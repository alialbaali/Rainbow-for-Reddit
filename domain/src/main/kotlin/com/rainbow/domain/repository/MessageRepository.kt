package com.rainbow.domain.repository

import com.rainbow.domain.models.Message

interface MessageRepository {

    suspend fun getInboxMessages(): Result<List<Message>>

    suspend fun getUnReadMessages(): Result<List<Message>>

    suspend fun getSentMessages(): Result<List<Message>>

    suspend fun createMessage(message: Message): Result<Message>

}
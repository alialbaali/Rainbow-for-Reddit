package com.rainbow.domain.repository

import com.rainbow.domain.models.Message

interface MessageRepository {

    suspend fun getInboxMessages(): Result<List<Message>>

    suspend fun getUnreadMessages(): Result<List<Message>>

    suspend fun getSentMessages(): Result<List<Message>>

    suspend fun getMessages(): Result<List<Message>>

    suspend fun getMentions(): Result<List<Message>>

    suspend fun getPostMessages(): Result<List<Message>>

    suspend fun getCommentMessages(): Result<List<Message>>

    suspend fun createMessage(message: Message): Result<Message>

}
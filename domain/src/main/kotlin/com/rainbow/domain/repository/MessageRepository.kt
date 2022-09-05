package com.rainbow.domain.repository

import com.rainbow.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    val inboxMessages: Flow<List<Message>>

    val unreadMessages: Flow<List<Message>>

    val sentMessages: Flow<List<Message>>

    val messages: Flow<List<Message>>

    val mentions: Flow<List<Message>>

    val postMessages: Flow<List<Message>>

    val commentMessages: Flow<List<Message>>

    suspend fun getInboxMessages(lastMessageId: String?): Result<Unit>

    suspend fun getUnreadMessages(lastMessageId: String?): Result<Unit>

    suspend fun getSentMessages(lastMessageId: String?): Result<Unit>

    suspend fun getMessages(lastMessageId: String?): Result<Unit>

    suspend fun getMentions(lastMessageId: String?): Result<Unit>

    suspend fun getPostMessages(lastMessageId: String?): Result<Unit>

    suspend fun getCommentMessages(lastMessageId: String?): Result<Unit>

    fun getMessage(messageId: String): Flow<Result<Message>>

    suspend fun createMessage(message: Message): Result<Message>

}
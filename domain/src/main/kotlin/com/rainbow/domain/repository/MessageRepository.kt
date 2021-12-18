package com.rainbow.domain.repository

import com.rainbow.domain.models.Message

interface MessageRepository {

    suspend fun getInboxMessages(lastMessageId: String?): Result<List<Message>>

    suspend fun getUnreadMessages(lastMessageId: String?): Result<List<Message>>

    suspend fun getSentMessages(lastMessageId: String?): Result<List<Message>>

    suspend fun getMessages(lastMessageId: String?): Result<List<Message>>

    suspend fun getMentions(lastMessageId: String?): Result<List<Message>>

    suspend fun getPostMessages(lastMessageId: String?): Result<List<Message>>

    suspend fun getCommentMessages(lastMessageId: String?): Result<List<Message>>

    suspend fun createMessage(message: Message): Result<Message>

}
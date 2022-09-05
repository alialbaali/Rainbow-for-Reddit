package com.rainbow.local

import com.rainbow.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface LocalMessageDataSource {

    val inboxMessages: Flow<List<Message>>

    val unreadMessages: Flow<List<Message>>

    val sentMessages: Flow<List<Message>>

    val messages: Flow<List<Message>>

    val mentions: Flow<List<Message>>

    val postMessages: Flow<List<Message>>

    val commentMessages: Flow<List<Message>>

    fun insertInboxMessage(message: Message)

    fun insertUnreadMessage(message: Message)

    fun insertSentMessage(message: Message)

    fun insertMessage(message: Message)

    fun insertMention(message: Message)

    fun insertPostMessage(message: Message)

    fun insertCommentMessage(message: Message)

    fun clearInboxMessage()

    fun clearUnreadMessage()

    fun clearSentMessages()

    fun clearMessages()

    fun clearMentions()

    fun clearPostMessages()

    fun clearCommentMessages()

}
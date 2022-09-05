package com.rainbow.local

import com.rainbow.domain.models.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class LocalMessageDataSourceImpl : LocalMessageDataSource {

    private val mutableInboxMessages = MutableStateFlow(emptyList<Message>())
    override val inboxMessages get() = mutableInboxMessages.asStateFlow()

    private val mutableUnreadMessages = MutableStateFlow(emptyList<Message>())
    override val unreadMessages get() = mutableUnreadMessages.asStateFlow()

    private val mutableSentMessages = MutableStateFlow(emptyList<Message>())
    override val sentMessages get() = mutableSentMessages.asStateFlow()

    private val mutableMessages = MutableStateFlow(emptyList<Message>())
    override val messages get() = mutableMessages.asStateFlow()

    private val mutableMentions = MutableStateFlow(emptyList<Message>())
    override val mentions get() = mutableMentions.asStateFlow()

    private val mutablePostMessages = MutableStateFlow(emptyList<Message>())
    override val postMessages get() = mutablePostMessages.asStateFlow()

    private val mutableCommentMessages = MutableStateFlow(emptyList<Message>())
    override val commentMessages get() = mutableCommentMessages.asStateFlow()

    private val allMessages = listOf(
        mutableInboxMessages,
        mutableUnreadMessages,
        mutableSentMessages,
        mutableMessages,
        mutableMentions,
        mutablePostMessages,
        mutableCommentMessages,
    )

    override fun getMessage(messageId: String): Flow<Message?> {
        return combine(allMessages) { arrayOfMessages ->
            arrayOfMessages.toList()
                .flatten()
                .find { message -> message.id == messageId }
        }
    }

    override fun insertInboxMessage(message: Message) {
        mutableInboxMessages.value += message
    }

    override fun insertUnreadMessage(message: Message) {
        mutableUnreadMessages.value += message
    }

    override fun insertSentMessage(message: Message) {
        mutableSentMessages.value += message
    }

    override fun insertMessage(message: Message) {
        mutableMessages.value += message
    }

    override fun insertMention(message: Message) {
        mutableMentions.value += message
    }

    override fun insertPostMessage(message: Message) {
        mutablePostMessages.value += message
    }

    override fun insertCommentMessage(message: Message) {
        mutableCommentMessages.value += message
    }

    override fun clearInboxMessage() {
        mutableInboxMessages.value = emptyList()
    }

    override fun clearUnreadMessage() {
        mutableUnreadMessages.value = emptyList()
    }

    override fun clearSentMessages() {
        mutableSentMessages.value = emptyList()
    }

    override fun clearMessages() {
        mutableMessages.value = emptyList()
    }

    override fun clearMentions() {
        mutableMentions.value = emptyList()
    }

    override fun clearPostMessages() {
        mutablePostMessages.value = emptyList()
    }

    override fun clearCommentMessages() {
        mutableCommentMessages.value = emptyList()
    }

}
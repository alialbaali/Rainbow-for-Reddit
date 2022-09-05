package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.domain.models.Message
import com.rainbow.domain.repository.MessageRepository
import com.rainbow.local.LocalMessageDataSource
import com.rainbow.remote.dto.RemoteMessage
import com.rainbow.remote.source.RemoteMessageDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class MessageRepositoryImpl(
    private val remoteMessageDataSource: RemoteMessageDataSource,
    private val localMessageDataSource: LocalMessageDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteMessage, Message>,
) : MessageRepository {

    override val inboxMessages: Flow<List<Message>> = localMessageDataSource.inboxMessages
    override val unreadMessages: Flow<List<Message>> = localMessageDataSource.unreadMessages
    override val sentMessages: Flow<List<Message>> = localMessageDataSource.sentMessages
    override val messages: Flow<List<Message>> = localMessageDataSource.messages
    override val mentions: Flow<List<Message>> = localMessageDataSource.mentions
    override val postMessages: Flow<List<Message>> = localMessageDataSource.postMessages
    override val commentMessages: Flow<List<Message>> = localMessageDataSource.commentMessages

    override suspend fun getInboxMessages(lastMessageId: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastMessageId == null) localMessageDataSource.clearInboxMessage()

            remoteMessageDataSource.getInbox(DefaultLimit, lastMessageId)
                .quickMap(mapper)
                .forEach(localMessageDataSource::insertInboxMessage)
        }
    }

    override suspend fun getUnreadMessages(lastMessageId: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastMessageId == null) localMessageDataSource.clearUnreadMessage()

            remoteMessageDataSource.getUnreadInbox(DefaultLimit, lastMessageId)
                .quickMap(mapper)
                .forEach(localMessageDataSource::insertUnreadMessage)
        }
    }

    override suspend fun getSentMessages(lastMessageId: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastMessageId == null) localMessageDataSource.clearSentMessages()

            remoteMessageDataSource.getSent(DefaultLimit, lastMessageId)
                .quickMap(mapper)
                .forEach(localMessageDataSource::insertSentMessage)
        }
    }

    override suspend fun getMessages(lastMessageId: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastMessageId == null) localMessageDataSource.clearMessages()

            remoteMessageDataSource.getMessages(DefaultLimit, lastMessageId)
                .quickMap(mapper)
                .forEach(localMessageDataSource::insertMessage)
        }
    }

    override suspend fun getMentions(lastMessageId: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastMessageId == null) localMessageDataSource.clearMentions()

            remoteMessageDataSource.getMentions(DefaultLimit, lastMessageId)
                .quickMap(mapper)
        }
    }

    override suspend fun getPostMessages(lastMessageId: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastMessageId == null) localMessageDataSource.clearPostMessages()

            remoteMessageDataSource.getPostReplies(DefaultLimit, lastMessageId)
                .quickMap(mapper)
                .forEach(localMessageDataSource::insertPostMessage)
        }
    }

    override suspend fun getCommentMessages(lastMessageId: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastMessageId == null) localMessageDataSource.clearCommentMessages()

            remoteMessageDataSource.getCommentReplies(DefaultLimit, lastMessageId)
                .quickMap(mapper)
                .forEach(localMessageDataSource::insertCommentMessage)
        }
    }

    override suspend fun createMessage(message: Message): Result<Message> {
        TODO("Not yet implemented")
    }
}
package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.domain.models.Message
import com.rainbow.domain.repository.MessageRepository
import com.rainbow.remote.dto.RemoteMessage
import com.rainbow.remote.source.RemoteMessageDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class MessageRepositoryImpl(
    private val remoteMessageDataSource: RemoteMessageDataSource,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteMessage, Message>,
) : MessageRepository {
    override suspend fun getInboxMessages(lastMessageId: String?): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getInbox(DefaultLimit, lastMessageId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getUnreadMessages(lastMessageId: String?): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getUnreadInbox(DefaultLimit, lastMessageId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getSentMessages(lastMessageId: String?): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getSent(DefaultLimit, lastMessageId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getMessages(lastMessageId: String?): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getMessages(DefaultLimit, lastMessageId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getMentions(lastMessageId: String?): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getMentions(DefaultLimit, lastMessageId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getPostMessages(lastMessageId: String?): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getPostReplies(DefaultLimit, lastMessageId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getCommentMessages(lastMessageId: String?): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getCommentReplies(DefaultLimit, lastMessageId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun createMessage(message: Message): Result<Message> {
        TODO("Not yet implemented")
    }
}
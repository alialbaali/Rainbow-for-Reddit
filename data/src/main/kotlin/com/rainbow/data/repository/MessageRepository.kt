package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
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
    override suspend fun getInboxMessages(): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getInbox()
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getUnreadMessages(): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getUnreadInbox()
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getSentMessages(): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getSent()
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getMessages(): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getMessages()
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getMentions(): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getMentions()
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getPostMessages(): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getPostReplies()
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getCommentMessages(): Result<List<Message>> = withContext(dispatcher) {
        remoteMessageDataSource.getCommentReplies()
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun createMessage(message: Message): Result<Message> {
        TODO("Not yet implemented")
    }
}
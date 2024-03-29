package com.rainbow.remote.impl

import com.rainbow.remote.Listing
import com.rainbow.remote.client.Clients
import com.rainbow.remote.dto.RemoteMessage
import com.rainbow.remote.getOrThrow
import com.rainbow.remote.impl.Endpoint.Messages
import com.rainbow.remote.source.RemoteMessageDataSource
import com.rainbow.remote.submitForm
import com.rainbow.remote.toList
import io.ktor.client.*
import io.ktor.client.request.*

class RemoteMessageDataSourceImpl(private val client: HttpClient = Clients.Reddit) : RemoteMessageDataSource {

    override suspend fun getInbox(limit: Int, after: String?): List<RemoteMessage> {
        return client.getOrThrow<Listing<RemoteMessage>>(Messages.Inbox) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun getUnreadInbox(limit: Int, after: String?): List<RemoteMessage> {
        return client.getOrThrow<Listing<RemoteMessage>>(Messages.UnreadInbox) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun getSent(limit: Int, after: String?): List<RemoteMessage> {
        return client.getOrThrow<Listing<RemoteMessage>>(Messages.Sent) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun getMessages(limit: Int, after: String?): List<RemoteMessage> {
        return client.getOrThrow<Listing<RemoteMessage>>(Messages.Messages) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun getMentions(limit: Int, after: String?): List<RemoteMessage> {
        return client.getOrThrow<Listing<RemoteMessage>>(Messages.Mentions) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun getPostReplies(limit: Int, after: String?): List<RemoteMessage> {
        return client.getOrThrow<Listing<RemoteMessage>>(Messages.PostReplies) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun getCommentReplies(limit: Int, after: String?): List<RemoteMessage> {
        return client.getOrThrow<Listing<RemoteMessage>>(Messages.CommentReplies) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun sendMessage(subject: String, text: String, toUserIdPrefixed: String): Result<Unit> {
        return client.submitForm(Messages.Compose) {
            parameter(Keys.Subject, subject)
            parameter(Keys.Text, text)
            parameter(Keys.To, toUserIdPrefixed)
        }
    }

    override suspend fun readMessage(messageId: String): Result<Unit> {
        return client.submitForm(Messages.Read) {
            parameter(Keys.Id, messageId)
        }
    }

    override suspend fun unreadMessage(messageId: String): Result<Unit> {
        return client.submitForm(Messages.Unread) {
            parameter(Keys.Id, messageId)
        }
    }

    override suspend fun readAllMessages(): Result<Unit> {
        return client.submitForm(Messages.ReadAll)
    }

    override suspend fun deleteMessage(messageId: String): Result<Unit> {
        return client.submitForm(Messages.Delete) {
            parameter(Keys.Id, messageId)
        }
    }

}
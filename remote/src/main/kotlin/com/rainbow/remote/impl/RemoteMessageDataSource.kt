package com.rainbow.remote.impl

import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteMessage
import com.rainbow.remote.get
import com.rainbow.remote.impl.Endpoint.Messages
import com.rainbow.remote.source.RemoteMessageDataSource
import com.rainbow.remote.submitForm
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteMessageDataSource(client: HttpClient = redditClient): RemoteMessageDataSource =
    RemoteMessageDataSourceImpl(client)

private class RemoteMessageDataSourceImpl(private val client: HttpClient) : RemoteMessageDataSource {

    override suspend fun getMessages(messagesSorting: String): Result<List<RemoteMessage>> {
        return client.get(Messages.Get(messagesSorting))
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
        return client.submitForm(Messages.UnRead) {
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
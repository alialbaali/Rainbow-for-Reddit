package com.rainbow.data.source.remote

import com.rainbow.remote.Endpoint.Messages
import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteMessage
import com.rainbow.remote.getValue
import com.rainbow.remote.redditGet
import com.rainbow.remote.redditSubmitForm
import com.rainbow.remote.source.RemoteMessageDataSource
import io.ktor.client.*
import io.ktor.client.request.*

internal fun RemoteMessageDataSource(client: HttpClient): RemoteMessageDataSource = RemoteMessageDataSourceImpl(client)

private class RemoteMessageDataSourceImpl(private val client: HttpClient) : RemoteMessageDataSource {

    override suspend fun getMessages(messagesSorting: String): RedditResponse<List<RemoteMessage>> {
        val path by Messages.Get(messagesSorting)
        return client.redditGet(path)
    }

    override suspend fun sendMessage(subject: String, text: String, toUserIdPrefixed: String): RedditResponse<Unit> {
        val path by Messages.Compose
        return client.redditSubmitForm(path) {
            parameter(Keys.Subject, subject)
            parameter(Keys.Text, text)
            parameter(Keys.To, toUserIdPrefixed)
        }
    }

    override suspend fun readMessage(messageId: String): RedditResponse<Unit> {
        val path by Messages.Read
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, messageId)
        }
    }

    override suspend fun unreadMessage(messageId: String): RedditResponse<Unit> {
        val path by Messages.UnRead
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, messageId)
        }
    }

    override suspend fun readAllMessages(): RedditResponse<Unit> {
        val path by Messages.ReadAll
        return client.redditSubmitForm(path)
    }

    override suspend fun deleteMessage(messageId: String): RedditResponse<Unit> {
        val path by Messages.Delete
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, messageId)
        }
    }

}
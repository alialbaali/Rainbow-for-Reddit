package com.rainbow.data.source.remote

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.client
import com.rainbow.remote.dto.RemoteMessage
import com.rainbow.remote.redditGet
import com.rainbow.remote.redditSubmitForm
import com.rainbow.remote.source.RemoteMessageDataSource
import io.ktor.client.*
import io.ktor.client.request.*

internal fun RemoteMessageDataSource(): RemoteMessageDataSource = RemoteMessageDataSourceImpl(client)

private class RemoteMessageDataSourceImpl(private val client: HttpClient) : RemoteMessageDataSource {

    override suspend fun getMessages(messagesSorting: String): RedditResponse<List<RemoteMessage>> {
        val url = "message/$messagesSorting"
        return client.redditGet(url)
    }

    override suspend fun sendMessage(subject: String, text: String, toUserIdPrefixed: String): RedditResponse<Unit> {
        val url = "api/compose"
        return client.redditSubmitForm(url) {
            parameter(Keys.Subject, subject)
            parameter(Keys.Text, text)
            parameter(Keys.To, toUserIdPrefixed)
        }
    }

    override suspend fun readMessage(messageId: String): RedditResponse<Unit> {
        val url = "api/read_message"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, messageId)
        }
    }

    override suspend fun unreadMessage(messageId: String): RedditResponse<Unit> {
        val url = "api/unread_message"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, messageId)
        }
    }

    override suspend fun readAllMessages(): RedditResponse<Unit> {
        val url = "api/read_all_messages"
        return client.redditSubmitForm(url)
    }

    override suspend fun deleteMessage(messageId: String): RedditResponse<Unit> {
        val url = "api/del_msg"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, messageId)
        }
    }

}
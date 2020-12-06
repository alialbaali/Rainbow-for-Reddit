package com.rainbow.remote.source

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteMessage

interface RemoteMessageDataSource {

    suspend fun getMessages(messagesSorting: String): RedditResponse<List<RemoteMessage>>

    suspend fun sendMessage(subject: String, text: String, toUserIdPrefixed: String): RedditResponse<Unit>

    suspend fun readMessage(messageId: String): RedditResponse<Unit>

    suspend fun unreadMessage(messageId: String): RedditResponse<Unit>

    suspend fun readAllMessages(): RedditResponse<Unit>

    suspend fun deleteMessage(messageId: String): RedditResponse<Unit>

}
package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteMessage

interface RemoteMessageDataSource {

    suspend fun getMessages(messagesSorting: String): Result<List<RemoteMessage>>

    suspend fun sendMessage(subject: String, text: String, toUserIdPrefixed: String): Result<Unit>

    suspend fun readMessage(messageId: String): Result<Unit>

    suspend fun unreadMessage(messageId: String): Result<Unit>

    suspend fun readAllMessages(): Result<Unit>

    suspend fun deleteMessage(messageId: String): Result<Unit>

}
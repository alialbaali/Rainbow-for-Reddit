package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteMessage

interface RemoteMessageDataSource {

    suspend fun getInbox(): Result<List<RemoteMessage>>

    suspend fun getUnreadInbox(): Result<List<RemoteMessage>>

    suspend fun getSent(): Result<List<RemoteMessage>>

    suspend fun getMessages(): Result<List<RemoteMessage>>

    suspend fun getMentions(): Result<List<RemoteMessage>>

    suspend fun getPostReplies(): Result<List<RemoteMessage>>

    suspend fun getCommentReplies(): Result<List<RemoteMessage>>

    suspend fun sendMessage(subject: String, text: String, toUserIdPrefixed: String): Result<Unit>

    suspend fun readMessage(messageId: String): Result<Unit>

    suspend fun unreadMessage(messageId: String): Result<Unit>

    suspend fun readAllMessages(): Result<Unit>

    suspend fun deleteMessage(messageId: String): Result<Unit>

}
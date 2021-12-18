package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteMessage

interface RemoteMessageDataSource {

    suspend fun getInbox(limit: Int, after: String?): Result<List<RemoteMessage>>

    suspend fun getUnreadInbox(limit: Int, after: String?): Result<List<RemoteMessage>>

    suspend fun getSent(limit: Int, after: String?): Result<List<RemoteMessage>>

    suspend fun getMessages(limit: Int, after: String?): Result<List<RemoteMessage>>

    suspend fun getMentions(limit: Int, after: String?): Result<List<RemoteMessage>>

    suspend fun getPostReplies(limit: Int, after: String?): Result<List<RemoteMessage>>

    suspend fun getCommentReplies(limit: Int, after: String?): Result<List<RemoteMessage>>

    suspend fun sendMessage(subject: String, text: String, toUserIdPrefixed: String): Result<Unit>

    suspend fun readMessage(messageId: String): Result<Unit>

    suspend fun unreadMessage(messageId: String): Result<Unit>

    suspend fun readAllMessages(): Result<Unit>

    suspend fun deleteMessage(messageId: String): Result<Unit>

}
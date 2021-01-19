package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteComment

interface RemoteCommentDataSource {

    suspend fun getPostComments(postIdPrefixed: String): Result<List<RemoteComment>>

    suspend fun getUserComments(userIdPrefixed: String): Result<List<RemoteComment>>

    suspend fun saveComment(commentIdPrefixed: String): Result<Unit>

    suspend fun unSaveComment(commentIdPrefixed: String): Result<Unit>

    suspend fun upvoteComment(commentId: String): Result<Unit>

    suspend fun unvoteComment(commentId: String): Result<Unit>

    suspend fun downvoteComment(commentId: String): Result<Unit>

}
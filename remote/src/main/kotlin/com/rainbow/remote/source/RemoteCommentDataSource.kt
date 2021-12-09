package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteComment

interface RemoteCommentDataSource {

    suspend fun getHomeComments(): Result<List<RemoteComment>>

    suspend fun getPostComments(
        postId: String,
        commentsSorting: String,
    ): Result<List<RemoteComment>>

    suspend fun getUserComments(userName: String): Result<List<RemoteComment>>

    suspend fun getMoreComments(
        postId: String,
        childrenIds: List<String>,
        commentsSorting: String,
    ): Result<List<RemoteComment>>

    suspend fun submitComment(postId: String?, parentCommentId: String?, text: String): Result<Unit>

    suspend fun deleteComment(commentId: String): Result<Unit>

    suspend fun editComment(commentId: String, text: String): Result<Unit>

    suspend fun enableInboxReplies(commentId: String): Result<Unit>

    suspend fun disableInboxReplies(commentId: String): Result<Unit>

    suspend fun saveComment(commentId: String): Result<Unit>

    suspend fun unSaveComment(commentId: String): Result<Unit>

    suspend fun upvoteComment(commentId: String): Result<Unit>

    suspend fun unvoteComment(commentId: String): Result<Unit>

    suspend fun downvoteComment(commentId: String): Result<Unit>

}
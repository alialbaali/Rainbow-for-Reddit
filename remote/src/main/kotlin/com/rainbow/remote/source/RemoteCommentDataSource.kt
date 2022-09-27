package com.rainbow.remote.source

import com.rainbow.remote.dto.RemoteComment

interface RemoteCommentDataSource {

    suspend fun getHomeComments(limit: Int, after: String?): List<RemoteComment>

    suspend fun getPostComments(
        postId: String,
        commentsSorting: String,
        limit: Int,
    ): List<RemoteComment>

    suspend fun getUserComments(
        userName: String,
        commentsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemoteComment>

    suspend fun getMoreComments(
        postId: String,
        childrenIds: List<String>,
        commentsSorting: String,
    ): List<RemoteComment>

    suspend fun getThreadComments(
        postId: String,
        parentId: String,
        commentsSorting: String,
        limit: Int,
    ): List<RemoteComment>

    suspend fun submitComment(postId: String?, parentCommentId: String?, text: String): Result<Unit>

    suspend fun deleteComment(commentId: String): Result<Unit>

    suspend fun editComment(commentId: String, text: String): Result<Unit>

    suspend fun enableInboxReplies(commentId: String): Result<Unit>

    suspend fun disableInboxReplies(commentId: String): Result<Unit>

    suspend fun saveComment(commentId: String)

    suspend fun unSaveComment(commentId: String)

    suspend fun upvoteComment(commentId: String)

    suspend fun unvoteComment(commentId: String)

    suspend fun downvoteComment(commentId: String)

}
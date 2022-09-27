package com.rainbow.domain.repository

import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.UserPostSorting
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    val homeComments: Flow<List<Comment>>

    val postComments: Flow<List<Comment>>

    val userComments: Flow<List<Comment>>

    val profileComments: Flow<List<Comment>>

    suspend fun getProfileComments(
        commentsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastCommentId: String?,
    ): Result<Unit>

    suspend fun getHomeComments(lastCommentId: String?): Result<Unit>

    suspend fun getUserComments(
        userName: String,
        commentsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastCommentId: String?,
    ): Result<Unit>

    suspend fun getPostComments(
        postId: String,
        commentsSorting: PostCommentSorting,
    ): Result<Unit>

    suspend fun getMoreComments(
        postId: String,
        commentId: String,
        children: List<String>,
        commentsSorting: PostCommentSorting,
    ): Result<Unit>

    suspend fun getThreadComments(
        postId: String,
        parentId: String,
        commentsSorting: PostCommentSorting,
    ): Result<Unit>

    suspend fun createComment(comment: Comment): Result<Comment>

    suspend fun deleteComment(commentId: String): Result<Unit>

    suspend fun upvoteComment(commentId: String): Result<Unit>

    suspend fun unvoteComment(commentId: String): Result<Unit>

    suspend fun downvoteComment(commentId: String): Result<Unit>

    suspend fun saveComment(commentId: String): Result<Unit>

    suspend fun unSaveComment(commentId: String): Result<Unit>

}
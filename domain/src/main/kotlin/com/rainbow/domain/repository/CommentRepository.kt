package com.rainbow.domain.repository

import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.UserPostSorting
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    val comments: Flow<List<Comment>>

    suspend fun getCurrentUserComments(
        commentsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastCommentId: String?,
    ): Result<List<Comment>>

    suspend fun getHomeComments(lastCommentId: String?): Result<Unit>

    suspend fun getPostsComments(
        postId: String,
        commentsSorting: PostCommentSorting,
    ): Result<List<Comment>>

    suspend fun getUserComments(
        userName: String,
        commentsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastCommentId: String?,
    ): Result<List<Comment>>

    suspend fun getMorePostComments(
        postId: String,
        children: List<String>,
        commentsSorting: PostCommentSorting,
    ): Result<List<Comment>>

    suspend fun getThreadComments(
        postId: String,
        parentId: String,
        commentsSorting: PostCommentSorting,
    ): Result<List<Comment>>

    suspend fun createComment(comment: Comment): Result<Comment>

    suspend fun deleteComment(commentId: String): Result<Unit>

    suspend fun upvoteComment(commentId: String): Result<Unit>

    suspend fun unvoteComment(commentId: String): Result<Unit>

    suspend fun downvoteComment(commentId: String): Result<Unit>

}
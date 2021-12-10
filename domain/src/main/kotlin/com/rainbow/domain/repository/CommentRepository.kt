package com.rainbow.domain.repository

import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting

interface CommentRepository {

    suspend fun getCurrentUserComments(): Result<List<Comment>>

    suspend fun getHomeComments(lastCommentId: String?): Result<List<Comment>>

    suspend fun getPostsComments(
        postId: String,
        commentsSorting: PostCommentSorting,
    ): Result<List<Comment>>

    suspend fun getUserComments(userName: String): Result<List<Comment>>

    suspend fun getMoreComments(
        postId: String,
        children: List<String>,
        commentsSorting: PostCommentSorting,
    ): Result<List<Comment>>

    suspend fun createComment(comment: Comment): Result<Comment>

    suspend fun deleteComment(commentId: String): Result<Unit>

    suspend fun upvoteComment(commentId: String): Result<Unit>

    suspend fun unvoteComment(commentId: String): Result<Unit>

    suspend fun downvoteComment(commentId: String): Result<Unit>

}
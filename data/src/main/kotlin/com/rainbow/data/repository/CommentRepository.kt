package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.source.RemoteCommentDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class CommentRepositoryImpl(
    private val remoteCommentDataSource: RemoteCommentDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteComment, Comment>,
) : CommentRepository {

    override suspend fun getCurrentUserComments(): Result<List<Comment>> {
        val currentUserId = settings.getString(SettingsKeys.UserName)
        return remoteCommentDataSource.getUserComments(currentUserId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getHomeComments(): Result<List<Comment>> {
        return remoteCommentDataSource.getHomeComments()
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getPostsComments(
        postId: String,
        commentsSorting: PostCommentSorting,
    ): Result<List<Comment>> {
        return remoteCommentDataSource.getPostComments(postId, commentsSorting.name.lowercase())
            .map { it.quickMap(mapper) }
    }

    override suspend fun getUserComments(userName: String): Result<List<Comment>> {
        return remoteCommentDataSource.getUserComments(userName)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getMoreComments(
        postId: String,
        children: List<String>,
        commentsSorting: PostCommentSorting,
    ): Result<List<Comment>> {
        return remoteCommentDataSource.getMoreComments(postId, children, commentsSorting.name.lowercase())
            .map { it.quickMap(mapper) }
    }

    override suspend fun createComment(comment: Comment): Result<Comment> = withContext(dispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun upvoteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        remoteCommentDataSource.upvoteComment(commentId)
    }

    override suspend fun unvoteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        remoteCommentDataSource.unvoteComment(commentId)
    }

    override suspend fun downvoteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        remoteCommentDataSource.downvoteComment(commentId)
    }
}
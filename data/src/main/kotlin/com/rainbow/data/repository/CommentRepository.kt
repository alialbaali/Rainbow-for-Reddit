package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.data.utils.lowercaseName
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.PostCommentSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.UserPostSorting
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

    override suspend fun getCurrentUserComments(
        commentsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastCommentId: String?,
    ): Result<List<Comment>> = withContext(dispatcher) {
        val currentUserId = settings.getString(SettingsKeys.UserName)
        remoteCommentDataSource.getUserComments(
            currentUserId,
            commentsSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastCommentId
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getHomeComments(lastCommentId: String?): Result<List<Comment>> = withContext(dispatcher) {
        remoteCommentDataSource.getHomeComments(DefaultLimit, lastCommentId)
            .mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getPostsComments(
        postId: String,
        commentsSorting: PostCommentSorting,
    ): Result<List<Comment>> = withContext(dispatcher) {
        remoteCommentDataSource.getPostComments(postId, commentsSorting.lowercaseName, DefaultLimit)
            .map { it.quickMap(mapper) }
    }

    override suspend fun getUserComments(
        userName: String,
        commentsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastCommentId: String?,
    ): Result<List<Comment>> = withContext(dispatcher) {
        remoteCommentDataSource.getUserComments(userName,
            commentsSorting.lowercaseName,
            timeSorting.lowercaseName,
            DefaultLimit,
            lastCommentId
        ).mapCatching { it.quickMap(mapper) }
    }

    override suspend fun getMorePostComments(
        postId: String,
        children: List<String>,
        commentsSorting: PostCommentSorting,
    ): Result<List<Comment>> = withContext(dispatcher) {
        remoteCommentDataSource.getMorePostComments(postId, children, commentsSorting.lowercaseName, DefaultLimit)
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
package com.rainbow.data.repository

import com.rainbow.data.Mapper
import com.rainbow.data.quickMap
import com.rainbow.data.utils.DefaultLimit
import com.rainbow.data.utils.SettingsKeys
import com.rainbow.data.utils.lowercaseName
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.local.LocalCommentDataSource
import com.rainbow.local.LocalItemDataSource
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.source.RemoteCommentDataSource
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@OptIn(ExperimentalSettingsApi::class)
internal class CommentRepositoryImpl(
    private val remoteCommentDataSource: RemoteCommentDataSource,
    private val localCommentDataSource: LocalCommentDataSource,
    private val localItemDataSource: LocalItemDataSource,
    private val settings: FlowSettings,
    private val dispatcher: CoroutineDispatcher,
    private val mapper: Mapper<RemoteComment, Comment>,
) : CommentRepository {

    override val homeComments: Flow<List<Comment>> = localCommentDataSource.homeComments
    override val postComments: Flow<List<Comment>> = localCommentDataSource.postComments
    override val userComments: Flow<List<Comment>> = localCommentDataSource.userComments
    override val profileComments: Flow<List<Comment>> = localCommentDataSource.profileComments

    override suspend fun getProfileComments(
        commentsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastCommentId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastCommentId == null) localCommentDataSource.clearProfileComments()

            val currentUserId = settings.getString(SettingsKeys.UserName)
            remoteCommentDataSource.getUserComments(
                currentUserId,
                commentsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastCommentId
            ).quickMap(mapper).forEach(localCommentDataSource::insertProfileComment)
        }
    }

    override suspend fun getHomeComments(lastCommentId: String?): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastCommentId == null) localCommentDataSource.clearHomeComments()
            remoteCommentDataSource.getHomeComments(DefaultLimit, lastCommentId)
                .quickMap(mapper)
                .forEach(localCommentDataSource::insertHomeComment)
        }
    }

    override suspend fun getUserComments(
        userName: String,
        commentsSorting: UserPostSorting,
        timeSorting: TimeSorting,
        lastCommentId: String?,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            if (lastCommentId == null) localCommentDataSource.clearUserComments()
            remoteCommentDataSource.getUserComments(
                userName,
                commentsSorting.lowercaseName,
                timeSorting.lowercaseName,
                DefaultLimit,
                lastCommentId
            ).quickMap(mapper).forEach(localCommentDataSource::insertUserComment)
        }
    }

    override suspend fun getPostsComments(
        postId: String,
        commentsSorting: PostCommentSorting,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            localCommentDataSource.clearPostComments()
            remoteCommentDataSource.getPostComments(
                postId,
                commentsSorting.lowercaseName,
                DefaultLimit
            ).quickMap(mapper).forEach(localCommentDataSource::insertPostComment)
        }
    }

    override suspend fun getViewMoreComments(
        postId: String,
        commentId: String,
        children: List<String>,
        commentsSorting: PostCommentSorting
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            val allComments = remoteCommentDataSource.getViewMoreComments(
                postId,
                children,
                commentsSorting.lowercaseName,
                DefaultLimit,
            ).quickMap(mapper)

            val newPostComments = allComments.filter { it.parentId == postId }
            val comments = postComments.first()
                .filterNot { it.id == commentId }
                .plus(newPostComments)
                .replaceRepliesViewMore(commentId, allComments)

            // TODO Update comments, instead of clearing and inserting them again.
            localCommentDataSource.clearPostComments()
            comments.forEach(localCommentDataSource::insertPostComment)
        }
    }

    override suspend fun getThreadComments(
        postId: String,
        parentId: String,
        commentsSorting: PostCommentSorting,
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            localCommentDataSource.clearThreadComments(parentId)
            remoteCommentDataSource.getThreadComments(
                postId,
                parentId,
                commentsSorting.lowercaseName,
                DefaultLimit,
            ).quickMap(mapper)
                .map { it.copy(isContinueThread = true) }
                .forEach(localCommentDataSource::insertPostComment)
        }
    }

    override suspend fun createComment(comment: Comment): Result<Comment> = withContext(dispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> = withContext(dispatcher) {
        TODO("Not yet implemented")
    }

    override suspend fun upvoteComment(commentId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remoteCommentDataSource.upvoteComment(commentId)
            localCommentDataSource.updateComment(commentId) { comment ->
                comment.copy(vote = Vote.Up)
            }
            localItemDataSource.updateItem<Comment>(commentId) { comment ->
                comment.copy(vote = Vote.Up)
            }
        }
    }

    override suspend fun unvoteComment(commentId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remoteCommentDataSource.unvoteComment(commentId)
            localCommentDataSource.updateComment(commentId) { comment ->
                comment.copy(vote = Vote.None)
            }
            localItemDataSource.updateItem<Comment>(commentId) { comment ->
                comment.copy(vote = Vote.None)
            }
        }
    }

    override suspend fun downvoteComment(commentId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remoteCommentDataSource.downvoteComment(commentId)
            localCommentDataSource.updateComment(commentId) { comment ->
                comment.copy(vote = Vote.Down)
            }
            localItemDataSource.updateItem<Comment>(commentId) { comment ->
                comment.copy(vote = Vote.Down)
            }
        }
    }

    override suspend fun saveComment(commentId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remoteCommentDataSource.saveComment(commentId)
            localCommentDataSource.updateComment(commentId) { comment ->
                comment.copy(isSaved = true)
            }
            localItemDataSource.updateItem<Comment>(commentId) { comment ->
                comment.copy(isSaved = true)
            }
        }
    }

    override suspend fun unSaveComment(commentId: String): Result<Unit> = runCatching {
        withContext(dispatcher) {
            remoteCommentDataSource.unSaveComment(commentId)
            localCommentDataSource.updateComment(commentId) { comment ->
                comment.copy(isSaved = false)
            }
            localItemDataSource.updateItem<Comment>(commentId) { comment ->
                comment.copy(isSaved = false)
            }
        }
    }

    private fun List<Comment>.replaceRepliesViewMore(
        commentId: String,
        comments: List<Comment>,
    ): List<Comment> {
        return map { comment ->
            val newReplies = comments.filter { it.parentId == comment.id }
            val replies = comment.replies.filterNot { it.id == commentId }
                .plus(newReplies)
            comment.copy(
                replies = replies.replaceRepliesViewMore(commentId, comments)
            )
        }
    }
}
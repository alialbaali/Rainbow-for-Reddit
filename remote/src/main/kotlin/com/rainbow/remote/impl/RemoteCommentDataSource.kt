package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.impl.Endpoint.Comments
import com.rainbow.remote.source.RemoteCommentDataSource
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteCommentDataSource(client: HttpClient = redditClient): RemoteCommentDataSource =
    RemoteCommentDataSourceImpl(client)

private typealias CommentsList = List<Item<Listing<RemoteComment>>>

private class RemoteCommentDataSourceImpl(val client: HttpClient) : RemoteCommentDataSource {

    override suspend fun getHomeComments(limit: Int, after: String?): List<RemoteComment> {
        return client.getOrThrow<Listing<RemoteComment>>(Comments.Home) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun getPostComments(
        postId: String,
        commentsSorting: String,
        limit: Int,
    ): List<RemoteComment> {
        return client.requestOrThrow<CommentsList>(Comments.PostComments(postId.removeIdPrefix())) {
            parameter(Keys.Sort, commentsSorting)
            parameter(Keys.Limit, limit)
        }.map { it.data.toList() }[1]
    }

    override suspend fun getUserComments(
        userName: String,
        commentsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): List<RemoteComment> {
        return client.getOrThrow<Listing<RemoteComment>>(Comments.UserComments(userName)) {
            parameter(Keys.Sort, commentsSorting)
            parameter(Keys.Time, timeSorting)
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun getViewMoreComments(
        postId: String,
        childrenIds: List<String>,
        commentsSorting: String,
        limit: Int,
    ): List<RemoteComment> {
        return client.requestOrThrow<Map<String, Item<Map<String, List<Item<RemoteComment>>>>>>(Comments.Replies) {
            parameter(Keys.Sort, commentsSorting)
            parameter(Keys.PostId, postId)
            parameter(Keys.ApiType, Values.Json)
            parameter(Keys.Children, childrenIds.take(limit).joinToString())
        }["json"]?.data?.get("things")?.map { it.data } ?: emptyList()
    }

    override suspend fun getThreadComments(
        postId: String,
        parentId: String,
        commentsSorting: String,
        limit: Int,
    ): List<RemoteComment> {
        return client.requestOrThrow<CommentsList>(
            Comments.ThreadComments(
                postId.removeIdPrefix(),
                parentId.removeIdPrefix()
            )
        ) {
            parameter(Keys.Sort, commentsSorting)
            parameter(Keys.Limit, limit)
        }.map { it.data.toList() }[1]
    }

    override suspend fun submitComment(postId: String?, parentCommentId: String?, text: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteComment(commentId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun editComment(commentId: String, text: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun enableInboxReplies(commentId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun disableInboxReplies(commentId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun saveComment(commentId: String) {
        return client.submitFormOrThrow(Comments.Save) {
            parameter(Keys.Id, commentId)
        }
    }

    override suspend fun unSaveComment(commentId: String) {
        return client.submitFormOrThrow(Comments.UnSave) {
            parameter(Keys.Id, commentId)
        }
    }

    override suspend fun upvoteComment(commentId: String) {
        return client.submitFormOrThrow(Comments.Upvote) {
            parameter(Keys.Id, commentId)
            parameter(Keys.Direction, Values.Upvote)
        }
    }

    override suspend fun unvoteComment(commentId: String) {
        return client.submitFormOrThrow(Comments.Unvote) {
            parameter(Keys.Id, commentId)
            parameter(Keys.Direction, Values.Unvote)
        }
    }

    override suspend fun downvoteComment(commentId: String) {
        return client.submitFormOrThrow(Comments.Downvote) {
            parameter(Keys.Id, commentId)
            parameter(Keys.Direction, Values.Downvote)
        }
    }

    private fun String.removeIdPrefix() = substringAfter('_')
}
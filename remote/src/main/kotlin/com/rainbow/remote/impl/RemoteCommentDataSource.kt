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

private class RemoteCommentDataSourceImpl(val client: HttpClient) : RemoteCommentDataSource {

    override suspend fun getHomeComments(limit: Int, after: String?): Result<List<RemoteComment>> {
        return client.get<Listing<RemoteComment>>(Comments.Home) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.mapCatching { it.toList() }
    }

    override suspend fun getPostComments(
        postId: String,
        commentsSorting: String,
        limit: Int,
    ): Result<List<RemoteComment>> {
        return client.plainRequest<List<Item<Listing<RemoteComment>>>>(Comments.PostComments(postId.asPostId())) {
            parameter(Keys.Sort, commentsSorting)
            parameter(Keys.Limit, limit)
        }.mapCatching { it.map { it.data.toList() }[1] }
    }

    override suspend fun getUserComments(
        userName: String,
        commentsSorting: String,
        timeSorting: String,
        limit: Int,
        after: String?,
    ): Result<List<RemoteComment>> {
        return client.get<Listing<RemoteComment>>(Comments.UserComments(userName)) {
            parameter(Keys.Sort, commentsSorting)
            parameter(Keys.Time, timeSorting)
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.mapCatching { it.toList() }
    }

    override suspend fun getMorePostComments(
        postId: String,
        childrenIds: List<String>,
        commentsSorting: String,
        limit: Int,
    ): Result<List<RemoteComment>> {
        return client.plainRequest<Map<String, Item<Map<String, List<Item<RemoteComment>>>>>>(Comments.Replies) {
            parameter(Keys.Sort, commentsSorting)
            parameter(Keys.PostId, postId)
            parameter(Keys.ApiType, Values.Json)
            parameter(Keys.Children, childrenIds.take(limit).joinToString())
        }.map { it["json"]?.data?.get("things")?.map { it.data } ?: emptyList() }
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

    override suspend fun saveComment(commentId: String): Result<Unit> {
        return client.submitForm(Comments.Save) {
            parameter(Keys.Id, commentId)
        }
    }

    override suspend fun unSaveComment(commentId: String): Result<Unit> {
        return client.submitForm(Comments.UnSave) {
            parameter(Keys.Id, commentId)
        }
    }

    override suspend fun upvoteComment(commentId: String): Result<Unit> {
        return client.submitForm(Comments.Upvote) {
            parameter(Keys.Id, commentId)
            parameter(Keys.Direction, Values.Upvote)
        }
    }

    override suspend fun unvoteComment(commentId: String): Result<Unit> {
        return client.submitForm(Comments.Unvote) {
            parameter(Keys.Id, commentId)
            parameter(Keys.Direction, Values.Unvote)
        }
    }

    override suspend fun downvoteComment(commentId: String): Result<Unit> {
        return client.submitForm(Comments.Downvote) {
            parameter(Keys.Id, commentId)
            parameter(Keys.Direction, Values.Downvote)
        }
    }

    private fun String.asPostId() = substringAfter('_')
}
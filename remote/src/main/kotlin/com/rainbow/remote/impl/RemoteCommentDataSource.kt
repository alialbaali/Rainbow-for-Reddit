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

    override suspend fun getPostComments(postId: String): Result<List<RemoteComment>> {
        return client.plainRequest<List<Item<Listing<RemoteComment>>>>(Comments.PostComments(postId.asPostId()))
            .mapCatching { it.map { it.data.toList() }[1] }
    }

    override suspend fun getUserComments(userId: String): Result<List<RemoteComment>> {
        return client.get<Listing<RemoteComment>>(Comments.UserComments(userId))
            .mapCatching { it.toList() }
    }

    override suspend fun getCommentReplies(sort: String): Result<List<RemoteComment>> {
        TODO("Not yet implemented")
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
package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.dto.Thing
import com.rainbow.remote.impl.Endpoint.Comments
import com.rainbow.remote.source.RemoteCommentDataSource
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteCommentDataSource(client: HttpClient = redditClient): RemoteCommentDataSource =
    RemoteCommentDataSourceImpl(client)

private class RemoteCommentDataSourceImpl(val client: HttpClient) : RemoteCommentDataSource {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getPostComments(postId: String): Result<List<RemoteComment>> {
        client.plainRequest<List<Item<Listing<Thing>>>>(Comments.PostComments(postId))
            .also(::println)
//            .mapCatching { it.getOrNull(1) as? Item<Listing<RemoteComment>>? }
//            .mapCatching { it?.data?.toList() ?: emptyList() }
        return Result.success(emptyList())
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

}

suspend fun main() {
    RemoteCommentDataSource()
        .getPostComments("lmktus")
        .getOrThrow()

}
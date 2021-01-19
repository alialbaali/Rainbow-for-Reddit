package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.impl.Endpoint.Comments
import com.rainbow.remote.source.RemoteCommentDataSource
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteCommentDataSource(client: HttpClient = mainClient): RemoteCommentDataSource =
    RemoteCommentDataSourceImpl(client)

private class RemoteCommentDataSourceImpl(val client: HttpClient) : RemoteCommentDataSource {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getPostComments(postIdPrefixed: String): Result<List<RemoteComment>> {
        return client.plainRequest<List<Map<String, Any>>>(Comments.PostComments(postIdPrefixed))
            .mapCatching { it.getOrElse(2) { emptyMap() } as Item<Listing<RemoteComment>> }
            .mapCatching { it.data.toList() }
    }

    override suspend fun getUserComments(userIdPrefixed: String): Result<List<RemoteComment>> {
        return client.get<Listing<RemoteComment>>(Comments.UserComments(userIdPrefixed))
            .mapCatching { it.toList() }
    }

    override suspend fun saveComment(commentIdPrefixed: String): Result<Unit> {
        return client.submitForm(Comments.Save) {
            parameter(Keys.Id, commentIdPrefixed)
        }
    }

    override suspend fun unSaveComment(commentIdPrefixed: String): Result<Unit> {
        return client.submitForm(Comments.UnSave) {
            parameter(Keys.Id, commentIdPrefixed)
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
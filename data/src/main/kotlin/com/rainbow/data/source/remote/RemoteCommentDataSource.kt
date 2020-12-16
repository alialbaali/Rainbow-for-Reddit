package com.rainbow.data.source.remote

import com.rainbow.remote.*
import com.rainbow.remote.Endpoint.Comments
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.source.RemoteCommentDataSource
import io.ktor.client.*
import io.ktor.client.request.*

internal fun RemoteCommentDataSource(client: HttpClient): RemoteCommentDataSource = RemoteCommentDataSourceImpl(client)

private class RemoteCommentDataSourceImpl(val client: HttpClient) : RemoteCommentDataSource {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getComments(postIdPrefixed: String): RedditResponse<List<RemoteComment>> {
        val path by Comments.Get(postIdPrefixed)
        return client.customRedditRequest<List<Map<String, Any>>>(path)
            ?.getOrNull(2)
            .let { it as RedditResponse<Listing<RemoteComment>> }
            .map { it.items }
    }

    override suspend fun saveComment(commentIdPrefixed: String): RedditResponse<Unit> {
        val path by Comments.Save
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, commentIdPrefixed)
        }
    }

    override suspend fun unSaveComment(commentIdPrefixed: String): RedditResponse<Unit> {
        val path by Comments.UnSave
        return client.redditSubmitForm(path) {
            parameter(Keys.Id, commentIdPrefixed)
        }
    }

}
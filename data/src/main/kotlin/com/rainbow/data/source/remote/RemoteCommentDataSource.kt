package com.rainbow.data.source.remote

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.source.RemoteCommentDataSource
import io.ktor.client.*

internal fun RemoteCommentDataSource(): RemoteCommentDataSource = RemoteCommentDataSourceImpl(client)

private class RemoteCommentDataSourceImpl(val client: HttpClient) : RemoteCommentDataSource {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getComments(postIdPrefixed: String): RedditResponse<List<RemoteComment>> {
        val url = "comments/$postIdPrefixed"
        return client.customRedditRequest<List<Map<String, Any>>>(url)
            ?.getOrNull(2)
            .let { it as RedditResponse<Listing<RemoteComment>> }
            .map { it.items }
    }
}
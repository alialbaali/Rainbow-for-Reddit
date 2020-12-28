package com.rainbow.remote.impl

import com.rainbow.remote.Item
import com.rainbow.remote.Listing
import com.rainbow.remote.dto.RemoteComment
import com.rainbow.remote.toList
import com.rainbow.remote.impl.Endpoint.Comments
import com.rainbow.remote.mainClient
import com.rainbow.remote.plainRequest
import com.rainbow.remote.source.RemoteCommentDataSource
import com.rainbow.remote.submitForm
import io.ktor.client.*
import io.ktor.client.request.*

fun RemoteCommentDataSource(client: HttpClient = mainClient): RemoteCommentDataSource =
    RemoteCommentDataSourceImpl(client)

private class RemoteCommentDataSourceImpl(val client: HttpClient) : RemoteCommentDataSource {

    @Suppress("UNCHECKED_CAST")
    override suspend fun getComments(postIdPrefixed: String): Result<List<RemoteComment>> {
        return client.plainRequest<List<Map<String, Any>>>(Comments.Get(postIdPrefixed))
            .mapCatching { it.getOrElse(2) { emptyMap()} as Item<Listing<RemoteComment>> }
            .mapCatching { it.data.toList() }
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

}
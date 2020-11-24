package com.rainbow.data.source.remote

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.source.RemotePostDataSource
import io.ktor.client.*
import io.ktor.client.request.*

internal fun RemotePostDataSource(): RemotePostDataSource = RemotePostDataSourceImpl(client)

private const val TimeParam = "t"

private class RemotePostDataSourceImpl(private val client: HttpClient) : RemotePostDataSource {


    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String,
    ): RedditResponse<Listing<RemotePost>> {
        val url = "r/$subredditName/${postsSorting}"
        return client.redditGet(url) {
            parameter(TimeParam, timeSorting)
        }
    }

    override suspend fun getUserPosts(
        username: String,
        postsSorting: String,
        timeSorting: String
    ): RedditResponse<Listing<RemotePost>> {
        val url = "$username/submitted/${postsSorting}"
        return client.redditGet(url) {
            parameter(TimeParam, timeSorting)
        }
    }

    override suspend fun upvotePost(postId: String) {
        val url = "api/vote"
        return client.redditSubmitForm(url) {
            parameter("id", postId)
            parameter("dir", 1)
        }
    }

    override suspend fun unvotePost(postId: String) {
        val url = "api/vote"
        return client.redditSubmitForm(url) {
            parameter("id", postId)
            parameter("dir", 0)
        }
    }

    override suspend fun downvotePost(postId: String) {
        val url = "api/vote"
        return client.redditSubmitForm(url) {
            parameter("id", postId)
            parameter("dir", -1)
        }
    }

    override suspend fun savePost(postId: String) {
        val url = "api/save"
        return client.redditSubmitForm(url) {
            parameter("id", postId)
        }
    }

    override suspend fun unSavePost(postId: String) {
        val url = "api/unsave"
        return client.redditSubmitForm(url) {
            parameter("id", postId)
        }
    }

    override suspend fun hidePost(postId: String) {
        val url = "api/hide"
        return client.redditSubmitForm(url) {
            parameter("id", postId)
        }
    }

    override suspend fun unHidePost(postId: String) {
        val url = "api/unhide"
        return client.redditSubmitForm(url) {
            parameter("id", postId)
        }
    }

}
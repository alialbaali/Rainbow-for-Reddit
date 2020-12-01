package com.rainbow.data.source.remote

import com.rainbow.remote.*
import com.rainbow.remote.dto.RemotePost
import com.rainbow.remote.source.RemotePostDataSource
import io.ktor.client.*
import io.ktor.client.request.*

internal fun RemotePostDataSource(): RemotePostDataSource = RemotePostDataSourceImpl(client)

private class RemotePostDataSourceImpl(private val client: HttpClient) : RemotePostDataSource {

    override suspend fun getMainPagePosts(
        mainPageSorting: String,
        timeSorting: String,
        lastPostIdPrefixed: String?
    ): RedditResponse<Listing<RemotePost>> {
        return client.redditGet(mainPageSorting) {
            parameter(Keys.Time, timeSorting)
            parameter(Keys.After, lastPostIdPrefixed)
        }
    }

    override suspend fun getSubredditPosts(
        subredditName: String,
        postsSorting: String,
        timeSorting: String,
    ): RedditResponse<Listing<RemotePost>> {
        val url = "r/$subredditName/${postsSorting}"
        return client.redditGet(url) {
            parameter(Keys.Time, timeSorting)
        }
    }

    override suspend fun getUserPosts(
        userName: String,
        postsSorting: String,
        timeSorting: String
    ): RedditResponse<Listing<RemotePost>> {
        val url = "user/$userName/submitted/${postsSorting}"
        return client.redditGet(url) {
            parameter(Keys.Time, timeSorting)
        }
    }

    override suspend fun upvotePost(postIdPrefixed: String): RedditResponse<Unit> {
        val url = "api/vote"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, postIdPrefixed)
            parameter(Keys.Direction, Values.Upvote)
        }
    }

    override suspend fun unvotePost(postIdPrefixed: String): RedditResponse<Unit> {
        val url = "api/vote"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, postIdPrefixed)
            parameter(Keys.Direction, Values.Unvote)
        }
    }

    override suspend fun downvotePost(postIdPrefixed: String): RedditResponse<Unit> {
        val url = "api/vote"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, postIdPrefixed)
            parameter(Keys.Direction, Values.Downvote)
        }
    }

    override suspend fun savePost(postIdPrefixed: String): RedditResponse<Unit> {
        val url = "api/save"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun unSavePost(postIdPrefixed: String): RedditResponse<Unit> {
        val url = "api/unsave"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun hidePost(postIdPrefixed: String): RedditResponse<Unit> {
        val url = "api/hide"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun unHidePost(postIdPrefixed: String): RedditResponse<Unit> {
        val url = "api/unhide"
        return client.redditSubmitForm(url) {
            parameter(Keys.Id, postIdPrefixed)
        }
    }

    override suspend fun submitPost(remotePost: RemotePost): RedditResponse<Unit> {
        val url = "api/submit"
        return client.redditSubmitForm(url) {
            TODO()
        }
    }

}
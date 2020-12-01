package com.rainbow.data.source.remote

import com.rainbow.remote.RedditResponse
import com.rainbow.remote.client
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.redditGet
import com.rainbow.remote.redditSubmitForm
import com.rainbow.remote.source.RemoteSubredditDataSource
import io.ktor.client.*
import io.ktor.client.request.*

internal fun RemoteSubredditDataSource(): RemoteSubredditDataSource = RemoteSubredditDataSourceImpl(client)

private class RemoteSubredditDataSourceImpl(private val client: HttpClient) : RemoteSubredditDataSource {

    override suspend fun getSubredditAbout(subredditName: String): RedditResponse<RemoteSubreddit> {
        val url = "r/$subredditName/about"
        return client.redditGet(url)
    }

    override suspend fun getMySubreddits(): RedditResponse<List<RemoteSubreddit>> {
        val url = "subreddits/mine/subscriber"
        return client.redditGet(url)
    }

    override suspend fun subscribeSubreddit(subredditId: String): RedditResponse<Unit> {
        val url = "api/subscribe"
        return client.redditSubmitForm(url) {
            parameter(Keys.Subreddit, subredditId)
            parameter(Keys.Action, Values.Sub)
        }
    }

    override suspend fun unSubscribeSubreddit(subredditId: String): RedditResponse<Unit> {
        val url = "api/subscribe"
        return client.redditSubmitForm(url) {
            parameter(Keys.Subreddit, subredditId)
            parameter(Keys.Action, Values.unSub)
        }
    }

    override suspend fun favoriteSubreddit(subredditName: String): RedditResponse<Unit> {
        val url = "api/favorite"
        return client.redditSubmitForm(url) {
            parameter(Keys.SubredditName, subredditName)
            parameter(Keys.Favorite, true)
        }
    }

    override suspend fun unFavoriteSubreddit(subredditName: String): RedditResponse<Unit> {
        val url = "api/favorite"
        return client.redditSubmitForm(url) {
            parameter(Keys.SubredditName, subredditName)
            parameter(Keys.Favorite, false)
        }
    }

}
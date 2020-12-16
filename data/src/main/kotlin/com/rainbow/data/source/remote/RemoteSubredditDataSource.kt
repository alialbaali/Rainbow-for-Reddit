package com.rainbow.data.source.remote

import com.rainbow.remote.Endpoint.Subreddits
import com.rainbow.remote.RedditResponse
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.getValue
import com.rainbow.remote.redditGet
import com.rainbow.remote.redditSubmitForm
import com.rainbow.remote.source.RemoteSubredditDataSource
import io.ktor.client.*
import io.ktor.client.request.*

internal fun RemoteSubredditDataSource(client: HttpClient): RemoteSubredditDataSource =
    RemoteSubredditDataSourceImpl(client)

private class RemoteSubredditDataSourceImpl(private val client: HttpClient) : RemoteSubredditDataSource {

    override suspend fun getSubredditAbout(subredditName: String): RedditResponse<RemoteSubreddit> {
        val path by Subreddits.About(subredditName)
        return client.redditGet(path)
    }

    override suspend fun getMySubreddits(): RedditResponse<List<RemoteSubreddit>> {
        val path by Subreddits.Mine
        return client.redditGet(path)
    }

    override suspend fun subscribeSubreddit(subredditId: String): RedditResponse<Unit> {
        val path by Subreddits.Subscribe
        return client.redditSubmitForm(path) {
            parameter(Keys.Subreddit, subredditId)
            parameter(Keys.Action, Values.Sub)
        }
    }

    override suspend fun unSubscribeSubreddit(subredditId: String): RedditResponse<Unit> {
        val path by Subreddits.UnSubscribe
        return client.redditSubmitForm(path) {
            parameter(Keys.Subreddit, subredditId)
            parameter(Keys.Action, Values.unSub)
        }
    }

    override suspend fun favoriteSubreddit(subredditName: String): RedditResponse<Unit> {
        val path by Subreddits.Favorite
        return client.redditSubmitForm(path) {
            parameter(Keys.SubredditName, subredditName)
            parameter(Keys.Favorite, true)
        }
    }

    override suspend fun unFavoriteSubreddit(subredditName: String): RedditResponse<Unit> {
        val path by Subreddits.UnFavorite
        return client.redditSubmitForm(path) {
            parameter(Keys.SubredditName, subredditName)
            parameter(Keys.Favorite, false)
        }
    }

}
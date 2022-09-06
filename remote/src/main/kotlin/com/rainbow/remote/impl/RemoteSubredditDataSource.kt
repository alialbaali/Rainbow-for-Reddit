package com.rainbow.remote.impl

import com.rainbow.remote.*
import com.rainbow.remote.client.redditClient
import com.rainbow.remote.dto.RemotePostRequirements
import com.rainbow.remote.dto.RemoteSubreddit
import com.rainbow.remote.impl.Endpoint.Subreddits
import com.rainbow.remote.source.RemoteSubredditDataSource
import io.ktor.client.*
import io.ktor.client.request.*

class RemoteSubredditDataSourceImpl(private val client: HttpClient = redditClient) : RemoteSubredditDataSource {

    override suspend fun getSubreddit(subredditName: String): RemoteSubreddit {
        return client.getOrThrow(Subreddits.About(subredditName))
    }

    override suspend fun getProfileSubreddits(limit: Int, after: String?): List<RemoteSubreddit> {
        return client.getOrThrow<Listing<RemoteSubreddit>>(Subreddits.Mine) {
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

    override suspend fun subscribeSubreddit(subredditId: String): Result<Unit> {
        return client.submitForm(Subreddits.Subscribe) {
            parameter(Keys.SubredditName, subredditId)
            parameter(Keys.Action, Values.Sub)
        }
    }

    override suspend fun unSubscribeSubreddit(subredditId: String): Result<Unit> {
        return client.submitForm(Subreddits.UnSubscribe) {
            parameter(Keys.SubredditName, subredditId)
            parameter(Keys.Action, Values.unSub)
        }
    }

    override suspend fun favoriteSubreddit(subredditName: String): Result<Unit> {
        return client.submitForm(Subreddits.Favorite) {
            parameter(Keys.SubredditName, subredditName)
            parameter(Keys.Favorite, true)
        }
    }

    override suspend fun unFavoriteSubreddit(subredditName: String): Result<Unit> {
        return client.submitForm(Subreddits.UnFavorite) {
            parameter(Keys.SubredditName, subredditName)
            parameter(Keys.Favorite, false)
        }
    }

    override suspend fun getSubredditSubmitText(subredditName: String): Result<String> {
        return client.plainRequest<Map<String, String>>(Subreddits.SubmitText(subredditName))
            .mapCatching { it.entries.first().value }
    }

    override suspend fun getSubredditPostRequirements(subredditName: String): Result<RemotePostRequirements> {
        return client.plainRequest(Subreddits.PostRequirements(subredditName))
    }

    override suspend fun searchSubreddit(
        subredditName: String,
        limit: Int,
        after: String?,
    ): List<RemoteSubreddit> {
        return client.getOrThrow<Listing<RemoteSubreddit>>(Subreddits.Search) {
            parameter(Keys.Query, subredditName)
            parameter(Keys.Limit, limit)
            parameter(Keys.After, after)
        }.toList()
    }

}
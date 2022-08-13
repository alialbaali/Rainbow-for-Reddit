package com.rainbow.local

import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalSubredditDataSourceImpl : LocalSubredditDataSource {

    private val mutableCurrentUserSubreddits = MutableStateFlow(emptyList<Subreddit>())
    override val currentUserSubreddits = mutableCurrentUserSubreddits.asStateFlow()

    override fun insertSubreddit(subreddit: Subreddit) {
        mutableCurrentUserSubreddits.value = currentUserSubreddits.value + subreddit
    }

    override fun subscribeSubreddit(subredditName: String) {
        mutableCurrentUserSubreddits.value = currentUserSubreddits.value.map { subreddit ->
            if (subreddit.name == subredditName)
                subreddit.copy(isSubscribed = true)
            else
                subreddit
        }
    }

    override fun unsubscribeSubreddit(subredditName: String) {
        mutableCurrentUserSubreddits.value = currentUserSubreddits.value.map { subreddit ->
            if (subreddit.name == subredditName)
                subreddit.copy(isSubscribed = false)
            else
                subreddit
        }
    }

    override fun favoriteSubreddit(subredditName: String) {
        mutableCurrentUserSubreddits.value = currentUserSubreddits.value.map { subreddit ->
            if (subreddit.name == subredditName)
                subreddit.copy(isFavorite = true)
            else
                subreddit
        }
    }

    override fun unFavoriteSubreddit(subredditName: String) {
        mutableCurrentUserSubreddits.value = currentUserSubreddits.value.map { subreddit ->
            if (subreddit.name == subredditName)
                subreddit.copy(isFavorite = false)
            else
                subreddit
        }
    }

    override fun clearSubreddits() {
        mutableCurrentUserSubreddits.value = emptyList()
    }

}
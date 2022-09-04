package com.rainbow.local

import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class LocalSubredditDataSourceImpl : LocalSubredditDataSource {

    private val mutableSubreddits = MutableStateFlow(emptyList<Subreddit>())
    override val subreddits get() = mutableSubreddits.asStateFlow()

    private val mutableProfileSubreddits = MutableStateFlow(emptyList<Subreddit>())
    override val profileSubreddits get() = mutableProfileSubreddits.asStateFlow()

    private val mutableSearchSubreddits = MutableStateFlow(emptyList<Subreddit>())
    override val searchSubreddits get() = mutableSearchSubreddits.asStateFlow()

    private val allSubreddits = listOf(
        mutableSubreddits,
        mutableProfileSubreddits,
        mutableSearchSubreddits,
    )

    override fun getSubreddit(subredditName: String): Flow<Subreddit?> {
        return combine(allSubreddits) { arrayOfSubreddits ->
            arrayOfSubreddits.toList()
                .flatten()
                .find { subreddit -> subreddit.name == subredditName }
        }
    }

    override fun insertSubreddit(subreddit: Subreddit) {
        mutableSubreddits.value = subreddits.value + subreddit
    }

    override fun insertProfileSubreddit(subreddit: Subreddit) {
        mutableProfileSubreddits.value = profileSubreddits.value + subreddit
    }

    override fun insertSearchSubreddit(subreddit: Subreddit) {
        mutableSearchSubreddits.value = searchSubreddits.value + subreddit
    }

    override fun subscribeSubreddit(subredditName: String) {
        allSubreddits.forEach { state ->
            state.value = state.value.map { subreddit ->
                if (subreddit.name == subredditName)
                    subreddit.copy(isSubscribed = true)
                else
                    subreddit
            }
        }
    }

    override fun unsubscribeSubreddit(subredditName: String) {
        allSubreddits.forEach { state ->
            state.value = state.value.map { subreddit ->
                if (subreddit.name == subredditName)
                    subreddit.copy(isSubscribed = false)
                else
                    subreddit
            }
        }
    }

    override fun favoriteSubreddit(subredditName: String) {
        allSubreddits.forEach { state ->
            state.value = state.value.map { subreddit ->
                if (subreddit.name == subredditName)
                    subreddit.copy(isFavorite = true)
                else
                    subreddit
            }
        }
    }

    override fun unFavoriteSubreddit(subredditName: String) {
        allSubreddits.forEach { state ->
            state.value = state.value.map { subreddit ->
                if (subreddit.name == subredditName)
                    subreddit.copy(isFavorite = false)
                else
                    subreddit
            }
        }
    }

    override fun clearSubreddits() {
        mutableSubreddits.value = emptyList()
    }

    override fun clearProfileSubreddits() {
        mutableProfileSubreddits.value = emptyList()
    }

    override fun clearSearchSubreddits() {
        mutableSearchSubreddits.value = emptyList()
    }

}
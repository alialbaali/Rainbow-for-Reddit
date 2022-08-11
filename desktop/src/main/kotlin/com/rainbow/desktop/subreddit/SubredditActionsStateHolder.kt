package com.rainbow.desktop.subreddit

import com.rainbow.desktop.model.StateHolder
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.launch

object SubredditActionsStateHolder : StateHolder() {

    fun subscribeSubreddit(subreddit: Subreddit, onSuccess: (Subreddit) -> Unit) = scope.launch {
        Repos.Subreddit.subscribeSubreddit(subreddit.name)
            .onSuccess { onSuccess(subreddit.copy(isSubscribed = true)) }
    }

    fun unSubscribeSubreddit(subreddit: Subreddit, onSuccess: (Subreddit) -> Unit) = scope.launch {
        Repos.Subreddit.unSubscribeSubreddit(subreddit.name)
            .onSuccess { onSuccess(subreddit.copy(isSubscribed = false)) }
    }

    fun favoriteSubreddit(subreddit: Subreddit, onSuccess: (Subreddit) -> Unit) = scope.launch {
        Repos.Subreddit.favoriteSubreddit(subreddit.name)
            .onSuccess { onSuccess(subreddit.copy(isFavorite = true)) }
    }

    fun unFavoriteSubreddit(subreddit: Subreddit, onSuccess: (Subreddit) -> Unit) = scope.launch {
        Repos.Subreddit.unFavoriteSubreddit(subreddit.name)
            .onSuccess { onSuccess(subreddit.copy(isFavorite = false)) }
    }

}
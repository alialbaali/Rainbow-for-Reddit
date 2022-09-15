package com.rainbow.desktop.subreddit

import com.rainbow.data.Repos
import com.rainbow.desktop.state.StateHolder
import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.launch

object SubredditActionsStateHolder : StateHolder() {

    fun subscribeSubreddit(subreddit: Subreddit) = scope.launch {
        Repos.Subreddit.subscribeSubreddit(subreddit.name)
    }

    fun unSubscribeSubreddit(subreddit: Subreddit) = scope.launch {
        Repos.Subreddit.unSubscribeSubreddit(subreddit.name)
    }

    fun favoriteSubreddit(subreddit: Subreddit) = scope.launch {
        Repos.Subreddit.favoriteSubreddit(subreddit.name)
    }

    fun unFavoriteSubreddit(subreddit: Subreddit) = scope.launch {
        Repos.Subreddit.unFavoriteSubreddit(subreddit.name)
    }

    fun selectFlair(subreddit: Subreddit, flairId: String) = scope.launch {
        Repos.Subreddit.selectFlair(subreddit.name, flairId)
    }

    fun unSelectFlair(subreddit: Subreddit) = scope.launch {
        Repos.Subreddit.unselectFlair(subreddit.name)
    }

}
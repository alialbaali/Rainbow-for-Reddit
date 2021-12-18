package com.rainbow.app.subreddit

import com.rainbow.app.model.Model
import com.rainbow.data.Repos

object CurrentUserSubredditsScreenModel : Model() {
    val subredditListModel = SubredditListModel { lastSubredditId ->
        Repos.Subreddit.getCurrentUserSubreddits(lastSubredditId)
            .map { it.sortedBy { subreddit -> subreddit.name } }
    }

    init {
        subredditListModel.loadItems()
    }
}
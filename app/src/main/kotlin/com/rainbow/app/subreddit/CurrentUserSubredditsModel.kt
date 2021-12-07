package com.rainbow.app.subreddit

import com.rainbow.app.utils.Model
import com.rainbow.data.Repos

object CurrentUserSubredditsModel : Model() {
    val subredditsModel = SubredditsModel { lastSubredditId ->
        Repos.Subreddit.getCurrentUserSubreddits(lastSubredditId)
    }
}
package com.rainbow.desktop.subreddit

import com.rainbow.desktop.model.UnSortedListStateHolder
import com.rainbow.domain.models.Subreddit

class SubredditListStateHolder(
    getSubreddits: suspend (String?) -> Result<List<Subreddit>>,
) : UnSortedListStateHolder<Subreddit>(getSubreddits) {
    override val Subreddit.itemId: String
        get() = this.id
}
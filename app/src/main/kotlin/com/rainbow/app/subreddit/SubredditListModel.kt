package com.rainbow.app.subreddit

import com.rainbow.app.model.UnSortedListModel
import com.rainbow.domain.models.Subreddit

class SubredditListModel(
    getSubreddits: suspend (String?) -> Result<List<Subreddit>>,
) : UnSortedListModel<Subreddit>(getSubreddits) {
    override val Subreddit.itemId: String
        get() = this.id
}
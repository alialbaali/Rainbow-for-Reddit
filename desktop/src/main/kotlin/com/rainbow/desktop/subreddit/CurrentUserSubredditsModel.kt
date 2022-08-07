package com.rainbow.desktop.subreddit

import com.rainbow.desktop.model.Model
import com.rainbow.desktop.subreddit.SubredditListModel
import com.rainbow.data.Repos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object CurrentUserSubredditsScreenModel : Model() {

    private val mutableSearchTerm = MutableStateFlow("")
    val searchTerm get() = mutableSearchTerm.asStateFlow()

    val subredditListModel = SubredditListModel { lastSubredditId ->
        Repos.Subreddit.getCurrentUserSubreddits(lastSubredditId)
            .map { it.sortedBy { subreddit -> subreddit.name } }
    }

    init {
        subredditListModel.loadItems()
    }

    fun setSearchTerm(searchTerm: String) {
        mutableSearchTerm.value = searchTerm
    }
}
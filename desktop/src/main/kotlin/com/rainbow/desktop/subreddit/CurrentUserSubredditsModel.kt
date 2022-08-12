package com.rainbow.desktop.subreddit

import com.rainbow.desktop.model.StateHolder
import com.rainbow.data.Repos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object CurrentUserSubredditsScreenStateHolder : StateHolder() {

    private val mutableSearchTerm = MutableStateFlow("")
    val searchTerm get() = mutableSearchTerm.asStateFlow()

//    val subredditListModel = SubredditListStateHolder { lastSubredditId ->
//        Repos.Subreddit.getCurrentUserSubreddits(lastSubredditId)
//            .map { it.sortedBy { subreddit -> subreddit.name } }
//    }

    init {
//        subredditListModel.loadItems()
    }

    fun setSearchTerm(searchTerm: String) {
        mutableSearchTerm.value = searchTerm
    }
}
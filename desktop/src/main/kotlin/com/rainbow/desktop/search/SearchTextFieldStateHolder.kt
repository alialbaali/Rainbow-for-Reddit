package com.rainbow.desktop.search

import com.rainbow.desktop.model.StateHolder
import com.rainbow.desktop.subreddit.SubredditListStateHolder
import com.rainbow.desktop.utils.Constants
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

private val subredditListResults = mutableMapOf<String, Result<List<Subreddit>>>()

@OptIn(FlowPreview::class)
object SearchTextFieldStateHolder : StateHolder() {

    private val mutableSearchTerm = MutableStateFlow("")
    val searchTerm get() = mutableSearchTerm.asStateFlow()

    val subredditListModel = SubredditListStateHolder { lastSubredditId ->
        subredditListResults.entries.find { it.key == searchTerm.value }?.value
            ?: Repos.Subreddit.searchSubreddits(searchTerm.value, lastSubredditId)
                .also { subredditListResults[searchTerm.value] = it }
    }

    init {
        searchTerm
            .debounce(Constants.RefreshContentDebounceTime)
            .filter { it.isNotBlank() }
            .onEach { subredditListModel.loadItems() }
            .launchIn(scope)
    }

    fun setSearchTerm(searchTerm: String) {
        mutableSearchTerm.value = searchTerm
    }
}
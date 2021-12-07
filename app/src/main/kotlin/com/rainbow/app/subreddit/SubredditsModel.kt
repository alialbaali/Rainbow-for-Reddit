package com.rainbow.app.subreddit

import com.rainbow.app.utils.Model
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.getOrDefault
import com.rainbow.app.utils.toUIState
import com.rainbow.domain.models.Subreddit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SubredditsModel(private val getSubreddits: suspend (String?) -> Result<List<Subreddit>>) : Model() {

    private val mutableSubreddits = MutableStateFlow<UIState<List<Subreddit>>>(UIState.Loading)
    val subreddits = mutableSubreddits.asStateFlow()

    private val mutableLastSubreddit = MutableStateFlow<Subreddit?>(null)
    val lastSubreddit get() = mutableLastSubreddit.asStateFlow()

    init {
        loadSubreddits()
    }

    private fun loadSubreddits() {
        scope.launch {
            mutableSubreddits.value = getSubreddits(lastSubreddit.value?.id)
                .map {
                    if (lastSubreddit.value != null)
                        subreddits.value.getOrDefault(emptyList()) + it
                    else
                        it
                }
                .toUIState()
        }
    }

    fun setLastSubreddit(subreddit: Subreddit) {
        mutableLastSubreddit.value = subreddit
        loadSubreddits()
    }

}
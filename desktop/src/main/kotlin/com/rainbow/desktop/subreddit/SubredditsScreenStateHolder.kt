package com.rainbow.desktop.subreddit

import com.rainbow.data.Repos
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.filterContent
import com.rainbow.desktop.utils.getOrDefault
import com.rainbow.desktop.utils.map
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.repository.SubredditRepository
import kotlinx.coroutines.flow.*

class SubredditsScreenStateHolder private constructor(
    private val subredditRepository: SubredditRepository = Repos.Subreddit,
) : StateHolder() {

    private val mutableSearchTerm = MutableStateFlow("")
    val searchTerm get() = mutableSearchTerm.asStateFlow()

    val subredditsStateHolder = object : SubredditsStateHolder(subredditRepository.profileSubreddits) {
        init {
            searchTerm
                .onEach { searchTerm ->
                    mutableItems.value = items.value.map {
                        it.filterContent(searchTerm)
                    }
                }
                .launchIn(scope)

            items
                .filter { it.isSuccess }
                .onEach {
                    val subreddits = it.getOrDefault(emptyList())
                    val lastSubreddit = subreddits.lastOrNull()
                    getItems(lastSubreddit)
                }
                .launchIn(scope)
        }

        override suspend fun getItems(lastItem: Subreddit?): Result<Unit> {
            return subredditRepository.getProfileSubreddits(lastItem?.id)
        }
    }

    init {
        if (subredditsStateHolder.items.value.isEmpty) subredditsStateHolder.loadItems()
    }

    companion object {
        val Instance = SubredditsScreenStateHolder()
    }

    fun setSearchTerm(searchTerm: String) {
        mutableSearchTerm.value = searchTerm
    }
}
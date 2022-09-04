package com.rainbow.desktop.subreddit

import com.rainbow.data.Repos
import com.rainbow.desktop.state.ListStateHolder
import com.rainbow.desktop.utils.filterContent
import com.rainbow.desktop.utils.map
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.repository.SubredditRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CurrentUserSubredditsScreenStateHolder private constructor(
    private val subredditRepository: SubredditRepository = Repos.Subreddit,
) : ListStateHolder<Subreddit>(subredditRepository.profileSubreddits) {

    private val mutableSearchTerm = MutableStateFlow("")
    val searchTerm get() = mutableSearchTerm.asStateFlow()

    init {
        loadItems()
        searchTerm
            .onEach { searchTerm ->
                mutableItems.value = items.value.map {
                    it.filterContent(searchTerm)
                }
            }
            .launchIn(scope)
    }

    companion object {
        private var stateHolder: CurrentUserSubredditsScreenStateHolder? = null

        fun getInstance(): CurrentUserSubredditsScreenStateHolder {
            return stateHolder ?: CurrentUserSubredditsScreenStateHolder().also {
                stateHolder = it
            }
        }
    }

    fun setSearchTerm(searchTerm: String) {
        mutableSearchTerm.value = searchTerm
    }

    override suspend fun getItems(lastItem: Subreddit?): Result<Unit> {
        return subredditRepository.getProfileSubreddits(lastItem?.id)
    }
}
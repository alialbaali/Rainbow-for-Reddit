package com.rainbow.desktop.search

import com.rainbow.data.Repos
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.subreddit.SubredditsStateHolder
import com.rainbow.desktop.user.UsersStateHolder
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.PostRepository
import com.rainbow.domain.repository.SubredditRepository
import com.rainbow.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class SearchScreenStateHolder private constructor(
    private val searchTerm: String,
    private val postRepository: PostRepository = Repos.Post,
    private val subredditRepository: SubredditRepository = Repos.Subreddit,
    private val userRepository: UserRepository = Repos.User,
) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(SearchTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val postsStateHolder = object : PostsStateHolder<SearchPostSorting>(
        SearchPostSorting.Default,
        postRepository.searchPosts,
    ) {
        override suspend fun getItems(
            sorting: SearchPostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.searchPosts(searchTerm, sorting, timeSorting, lastItem?.id)
    }

    val subredditsStateHolder = object : SubredditsStateHolder(subredditRepository.searchSubreddits) {
        override suspend fun getItems(lastItem: Subreddit?): Result<Unit> {
            return subredditRepository.searchSubreddits(searchTerm, lastItem?.id)
        }
    }

    val usersStateHolder = object : UsersStateHolder(userRepository.searchUsers) {
        override suspend fun getItems(lastItem: User?): Result<Unit> {
            return userRepository.searchUsers(searchTerm, lastItem?.id)
        }
    }

    companion object {
        private val stateHolders = mutableSetOf<SearchScreenStateHolder>()

        fun getInstance(searchTerm: String): SearchScreenStateHolder {
            return stateHolders.find { it.searchTerm == searchTerm }
                ?: SearchScreenStateHolder(searchTerm).also { stateHolders += it }
        }
    }

    init {
        selectedTab
            .onEach {
                when (it) {
                    SearchTab.Subreddits -> if (subredditsStateHolder.items.value.isEmpty) subredditsStateHolder.loadItems()
                    SearchTab.Posts -> if (postsStateHolder.items.value.isEmpty) postsStateHolder.loadItems()
                    SearchTab.Users -> if (usersStateHolder.items.value.isEmpty) usersStateHolder.loadItems()
                }
            }
            .launchIn(scope)
    }

    fun selectTab(tab: SearchTab) {
        mutableSelectedTab.value = tab
    }
}
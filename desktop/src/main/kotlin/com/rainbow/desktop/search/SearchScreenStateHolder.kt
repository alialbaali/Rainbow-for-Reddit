package com.rainbow.desktop.search

import com.rainbow.desktop.model.StateHolder
import com.rainbow.data.Repos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private val searchScreenModels = mutableSetOf<SearchScreenStateHolder>()

class SearchScreenStateHolder private constructor(private val searchTerm: String) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(SearchTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val initialPostSorting = Repos.Settings.getSearchPostSorting()

//    val postListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
//        Repos.Post.searchPosts(searchTerm, postSorting, timeSorting, lastPostId)
//    }

//    val subredditListModel = SubredditListStateHolder { lastSubredditId ->
//        Repos.Subreddit.searchSubreddits(searchTerm, lastSubredditId)
//    }

//    val userListModel = UserListStateHolder { lastUserId ->
//        Repos.User.searchUsers(searchTerm, lastUserId)
//    }

    companion object {
        fun getOrCreateInstance(searchTerm: String): SearchScreenStateHolder {
            return searchScreenModels.find { it.searchTerm == searchTerm }
                ?: SearchScreenStateHolder(searchTerm).also { searchScreenModels += it }
        }
    }

    init {
//        selectedTab
//            .onEach {
//                when (it) {
//                    SearchTab.Subreddits -> if (subredditListModel.items.value.isLoading) subredditListModel.loadItems()
//                    SearchTab.Posts -> if (postListModel.items.value.isLoading) postListModel.loadItems()
//                    SearchTab.Users -> if (userListModel.items.value.isLoading) userListModel.loadItems()
//                }
//            }
//            .launchIn(scope)
    }

    fun selectTab(tab: SearchTab) {
        mutableSelectedTab.value = tab
    }
}
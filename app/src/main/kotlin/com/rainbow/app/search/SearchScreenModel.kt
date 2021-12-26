package com.rainbow.app.search

import com.rainbow.app.model.Model
import com.rainbow.app.post.PostListModel
import com.rainbow.app.subreddit.SubredditListModel
import com.rainbow.app.user.UserListModel
import com.rainbow.data.Repos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private val searchScreenModels = mutableSetOf<SearchScreenModel>()

class SearchScreenModel private constructor(private val searchTerm: String) : Model() {

    private val mutableSelectedTab = MutableStateFlow(SearchTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val initialPostSorting = Repos.Settings.getSearchPostSorting()

    val postListModel = PostListModel(initialPostSorting) { postSorting, timeSorting, lastPostId ->
        Repos.Post.searchPosts(searchTerm, postSorting, timeSorting, lastPostId)
    }

    val subredditListModel = SubredditListModel { lastSubredditId ->
        Repos.Subreddit.searchSubreddits(searchTerm, lastSubredditId)
    }

    val userListModel = UserListModel { lastUserId ->
        Repos.User.searchUsers(searchTerm, lastUserId)
    }

    companion object {
        fun getOrCreateInstance(searchTerm: String): SearchScreenModel {
            return searchScreenModels.find { it.searchTerm == searchTerm }
                ?: SearchScreenModel(searchTerm).also { searchScreenModels += it }
        }
    }

    init {
        selectedTab
            .onEach {
                when (it) {
                    SearchTab.Subreddits -> if (subredditListModel.items.value.isLoading) subredditListModel.loadItems()
                    SearchTab.Posts -> if (postListModel.items.value.isLoading) postListModel.loadItems()
                    SearchTab.Users -> if (userListModel.items.value.isLoading) userListModel.loadItems()
                }
            }
            .launchIn(scope)
    }

    fun selectTab(tab: SearchTab) {
        mutableSelectedTab.value = tab
    }
}
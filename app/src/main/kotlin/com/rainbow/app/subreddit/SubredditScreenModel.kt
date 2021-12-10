package com.rainbow.app.subreddit

import com.rainbow.app.model.Model
import com.rainbow.app.post.PostListModel
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val subredditScreenModels = mutableSetOf<SubredditScreenModel>()

class SubredditScreenModel private constructor(private val subredditName: String) : Model() {

    private val mutableSelectedTab = MutableStateFlow(SubredditTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val mutableSubreddit = MutableStateFlow<UIState<Subreddit>>(UIState.Loading)
    val subreddit get() = mutableSubreddit.asStateFlow()

    private val mutableWiki = MutableStateFlow<UIState<WikiPage>>(UIState.Loading)
    val wiki get() = mutableWiki.asStateFlow()

    private val mutableModerators = MutableStateFlow<UIState<List<Moderator>>>(UIState.Loading)
    val moderators get() = mutableModerators.asStateFlow()

    private val mutableRules = MutableStateFlow<UIState<List<Rule>>>(UIState.Loading)
    val rules get() = mutableRules.asStateFlow()

    val postListModel = PostListModel(SubredditPostSorting.Controversial) { postSorting, timeSorting, lastPostId ->
        Repos.Post.getSubredditPosts(
            subredditName,
            postSorting,
            timeSorting,
            lastPostId
        )
    }

    init {
        loadSubreddit()
    }

    companion object {
        fun getOrCreateInstance(subredditName: String): SubredditScreenModel {
            return subredditScreenModels.find { it.subredditName == subredditName }
                ?: SubredditScreenModel(subredditName).also { subredditScreenModels += it }
        }
    }

    fun loadSubreddit() = scope.launch {
        mutableSubreddit.value = Repos.Subreddit.getSubreddit(subredditName)
            .onSuccess { postListModel.loadItems() }
            .toUIState()
    }

    fun selectTab(tab: SubredditTab) {
        mutableSelectedTab.value = tab
    }

    fun loadRules() = scope.launch {
        mutableRules.value = Repos.Subreddit.getSubredditRules(subredditName)
            .toUIState()
    }

    fun loadWiki() = scope.launch {
        mutableWiki.value = Repos.Subreddit.getWikiIndex(subredditName)
            .toUIState()
    }

    fun loadModerators() = scope.launch {
        mutableModerators.value = Repos.Subreddit.getSubredditModerators(subredditName)
            .toUIState()
    }
}
package com.rainbow.desktop.subreddit

import com.rainbow.data.Repos
import com.rainbow.desktop.model.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.map
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.Moderator
import com.rainbow.domain.models.Rule
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.WikiPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private val subredditScreenModels = mutableSetOf<SubredditScreenStateHolder>()

class SubredditScreenStateHolder private constructor(private val subredditName: String) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(SubredditTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val mutableSubreddit = MutableStateFlow<UIState<Subreddit>>(UIState.Empty)
    val subreddit get() = mutableSubreddit.asStateFlow()

    private val mutableWiki = MutableStateFlow<UIState<WikiPage>>(UIState.Empty)
    val wiki get() = mutableWiki.asStateFlow()

    private val mutableModerators = MutableStateFlow<UIState<List<Moderator>>>(UIState.Empty)
    val moderators get() = mutableModerators.asStateFlow()

    private val mutableRules = MutableStateFlow<UIState<List<Rule>>>(UIState.Empty)
    val rules get() = mutableRules.asStateFlow()

    private val initialPostSorting = Repos.Settings.getSubredditPostSorting()

//    val postListModel = PostListStateHolder(initialPostSorting) { postSorting, timeSorting, lastPostId ->
//        Repos.Post.getSubredditPosts(subredditName, postSorting, timeSorting, lastPostId)
//    }

    init {
        loadSubreddit()
//        selectedTab
//            .onEach {
//                when (it) {
//                    SubredditTab.Posts -> if (postListModel.items.value.isLoading) postListModel.loadItems()
//                    SubredditTab.Wiki -> if (wiki.value.isLoading) loadWiki()
//                    SubredditTab.Rules -> if (rules.value.isLoading) loadRules()
//                    SubredditTab.Moderators -> if (moderators.value.isLoading) loadModerators()
//                    SubredditTab.Description -> {}
//                }
//            }
//            .launchIn(scope)
    }

    companion object {
        fun getOrCreateInstance(subredditName: String): SubredditScreenStateHolder {
            return subredditScreenModels.find { it.subredditName == subredditName }
                ?: SubredditScreenStateHolder(subredditName).also { subredditScreenModels += it }
        }

        fun updateSubreddit(subreddit: Subreddit) {
            subredditScreenModels.onEach {
                it.mutableSubreddit.value = it.subreddit.value.map {
                    if (it.name == subreddit.name)
                        subreddit
                    else
                        it
                }
            }
        }
    }

    fun loadSubreddit() = scope.launch {
        mutableSubreddit.value = Repos.Subreddit.getSubreddit(subredditName)
//            .onSuccess { postListModel.loadItems() }
            .toUIState()
    }

    fun selectTab(tab: SubredditTab) {
        mutableSelectedTab.value = tab
    }

    private fun loadRules() = scope.launch {
        mutableRules.value = Repos.Subreddit.getSubredditRules(subredditName).toUIState(emptyList())
    }

    private fun loadWiki() = scope.launch {
        mutableWiki.value = Repos.Subreddit.getWikiIndex(subredditName).toUIState(null)
    }

    private fun loadModerators() = scope.launch {
        mutableModerators.value = Repos.Subreddit.getSubredditModerators(subredditName).toUIState()
    }
}
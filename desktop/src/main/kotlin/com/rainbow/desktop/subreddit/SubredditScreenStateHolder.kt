package com.rainbow.desktop.subreddit

import com.rainbow.data.Repos
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.PostRepository
import com.rainbow.domain.repository.SubredditRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SubredditScreenStateHolder private constructor(
    private val subredditName: String,
    private val postRepository: PostRepository = Repos.Post,
    private val subredditRepository: SubredditRepository = Repos.Subreddit,
) : StateHolder() {

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

    val postsStateHolder =
        object : PostsStateHolder<SubredditPostSorting>(SubredditPostSorting.Default, postRepository.posts) {
            override suspend fun getItems(
                sorting: SubredditPostSorting,
                timeSorting: TimeSorting,
                lastItem: Post?
            ): Result<Unit> = postRepository.getSubredditPosts(subredditName, sorting, timeSorting, lastItem?.id)
        }

    init {
        loadSubreddit()
        selectedTab
            .onEach {
                when (it) {
                    SubredditTab.Posts -> if (postsStateHolder.items.value.isEmpty) postsStateHolder.loadItems()
                    SubredditTab.Wiki -> if (wiki.value.isEmpty) loadWiki()
                    SubredditTab.Rules -> if (rules.value.isEmpty) loadRules()
                    SubredditTab.Moderators -> if (moderators.value.isEmpty) loadModerators()
                    SubredditTab.Description -> {}
                }
            }
            .launchIn(scope)
    }

    companion object {
        private val stateHolders = mutableSetOf<SubredditScreenStateHolder>()

        fun getInstance(subredditName: String): SubredditScreenStateHolder {
            return stateHolders.find { it.subredditName == subredditName }
                ?: SubredditScreenStateHolder(subredditName).also { stateHolders += it }
        }
    }

    private fun loadSubreddit() = scope.launch {
        mutableSubreddit.value = subredditRepository.getSubreddit(subredditName)
            .toUIState()
    }

    fun selectTab(tab: SubredditTab) {
        mutableSelectedTab.value = tab
    }

    private fun loadRules() = scope.launch {
        mutableRules.value = subredditRepository.getSubredditRules(subredditName).toUIState(emptyList())
    }

    private fun loadWiki() = scope.launch {
        mutableWiki.value = subredditRepository.getWikiIndex(subredditName).toUIState(null)
    }

    private fun loadModerators() = scope.launch {
        mutableModerators.value = subredditRepository.getSubredditModerators(subredditName).toUIState()
    }
}
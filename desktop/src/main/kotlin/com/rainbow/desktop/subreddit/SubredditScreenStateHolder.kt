package com.rainbow.desktop.subreddit

import com.rainbow.data.Repos
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrNull
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.PostRepository
import com.rainbow.domain.repository.SubredditRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SubredditScreenStateHolder constructor(
    private val subredditName: String,
    private val postRepository: PostRepository = Repos.Post,
    private val subredditRepository: SubredditRepository = Repos.Subreddit,
) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(SubredditTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val subreddit = subredditRepository.getSubreddit(subredditName)
        .map { it.toUIState() }
        .stateIn(scope, SharingStarted.Eagerly, UIState.Loading())

    private val mutableWiki = MutableStateFlow<UIState<WikiPage>>(UIState.Empty)
    val wiki get() = mutableWiki.asStateFlow()

    private val mutableModerators = MutableStateFlow<UIState<List<Moderator>>>(UIState.Empty)
    val moderators get() = mutableModerators.asStateFlow()

    private val mutableRules = MutableStateFlow<UIState<List<Rule>>>(UIState.Empty)
    val rules get() = mutableRules.asStateFlow()

    private val mutableFlairs = MutableStateFlow<UIState<List<Pair<Flair, Boolean>>>>(UIState.Empty)
    val flairs get() = mutableFlairs.asStateFlow()

    val isFlairsEnabled = subredditRepository.isFlairsEnabled.stateIn(scope, SharingStarted.Eagerly, false)

    val postsStateHolder = object : PostsStateHolder<SubredditPostSorting>(
        SubredditPostSorting.Default,
        postRepository.subredditPosts,
    ) {
        override suspend fun getItems(
            sorting: SubredditPostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.getSubredditPosts(subredditName, sorting, timeSorting, lastItem?.id)
    }

    private val mutableSelectedItemId = MutableStateFlow<String?>(null)
    val selectedItemId get() = mutableSelectedItemId.asStateFlow()

    init {
        selectedTab
            .onEach {
                when (it) {
                    SubredditTab.Posts -> if (postsStateHolder.items.value.isEmpty) postsStateHolder.loadItems()
                    SubredditTab.Wiki -> if (wiki.value.isEmpty) loadWiki()
                    SubredditTab.Rules -> if (rules.value.isEmpty) loadRules()
                    SubredditTab.Moderators -> if (moderators.value.isEmpty) loadModerators()
                    SubredditTab.About -> {}
                }
            }
            .launchIn(scope)

        scope.launch {
            postsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { post -> selectItemId(post.id) }
        }
    }

    companion object {
        private val stateHolders = mutableSetOf<SubredditScreenStateHolder>()
        var CurrentInstance: SubredditScreenStateHolder? = null
            private set

        fun getInstance(subredditName: String): SubredditScreenStateHolder {
            val stateHolder = stateHolders.find { it.subredditName == subredditName }
                ?: SubredditScreenStateHolder(subredditName).also { stateHolders += it }
            stateHolder.reloadContentIfNeeded()
            return stateHolder
        }

        private fun SubredditScreenStateHolder.reloadContentIfNeeded() {
            if (CurrentInstance?.subredditName != this.subredditName) {
                val reloadPosts = !postsStateHolder.items.value.isEmpty
                if (reloadPosts) this.postsStateHolder.loadItems()
                CurrentInstance = this
            }
        }
    }

    fun selectTab(tab: SubredditTab) {
        mutableSelectedTab.value = tab
    }

    fun loadRules() = scope.launch {
        mutableRules.value = UIState.Loading()
        mutableRules.value = subredditRepository.getSubredditRules(subredditName).toUIState(emptyList())
    }

    fun loadWiki() = scope.launch {
        mutableWiki.value = UIState.Loading()
        mutableWiki.value = subredditRepository.getWikiIndex(subredditName).toUIState(null)
    }

    fun loadModerators() = scope.launch {
        mutableModerators.value = UIState.Loading()
        mutableModerators.value = subredditRepository.getSubredditModerators(subredditName).toUIState()
    }

    fun loadFlairs() = scope.launch {
        if (flairs.value.isEmpty) {
            mutableFlairs.value = UIState.Loading()
            launch {
                subredditRepository.getCurrentSubredditFlair(subredditName)
                    .onFailure { mutableFlairs.value = UIState.Failure(exception = it) }
            }
            subredditRepository.getSubredditFlairs(subredditName)
                .fold(
                    onSuccess = {
                        subredditRepository.flairs
                            .combine(subredditRepository.currentFlair) { flairs, currentFlair ->
                                val selectableFlairs = flairs.map { flair ->
                                    val isSelected = flair.id == currentFlair?.id
                                    flair to isSelected
                                }.sortedByDescending { it.second }
                                mutableFlairs.value = UIState.Success(selectableFlairs)
                            }
                            .launchIn(scope)
                    },
                    onFailure = {
                        mutableFlairs.value = UIState.Failure(exception = it)
                    }
                )
        }
    }

    fun selectFlair(flair: Flair) = scope.launch {
        subredditRepository.selectFlair(subredditName, flair.id)
    }

    fun clearFlair() = scope.launch {
        subredditRepository.unselectFlair(subredditName)
    }

    fun enableFlairs() = scope.launch {
        subredditRepository.enableFlairs(subredditName)
    }

    fun disableFlairs() = scope.launch {
        subredditRepository.disableFlairs(subredditName)
    }

    fun selectItemId(id: String) {
        mutableSelectedItemId.value = id
    }
}
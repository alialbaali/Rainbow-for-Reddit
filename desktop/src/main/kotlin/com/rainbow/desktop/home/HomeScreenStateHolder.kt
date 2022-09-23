package com.rainbow.desktop.home

import com.rainbow.data.Repos
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrNull
import com.rainbow.domain.models.*
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.domain.repository.PostRepository
import com.rainbow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeScreenStateHolder private constructor(
    private val postRepository: PostRepository = Repos.Post,
    private val commentRepository: CommentRepository = Repos.Comment,
    private val settingsRepository: SettingsRepository = Repos.Settings,
) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(HomeTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val postLayout = settingsRepository.postLayout.stateIn(scope, SharingStarted.Eagerly, PostLayout.Default)

    val postsStateHolder = object : PostsStateHolder<HomePostSorting>(
        HomePostSorting.Default,
        postRepository.homePosts,
    ) {
        override suspend fun getItems(
            sorting: HomePostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.getHomePosts(sorting, timeSorting, lastItem?.id)
    }

    val commentsStateHolder = object : HomeScreenCommentsStateHolder(commentRepository.homeComments) {
        override suspend fun getItems(lastItem: Comment?): Result<Unit> {
            return commentRepository.getHomeComments(lastItem?.id)
        }
    }

    private val mutableSelectedItemIds = MutableStateFlow(emptyMap<HomeTab, String>())
    val selectedItemIds get() = mutableSelectedItemIds.asStateFlow()

    init {
        selectedTab
            .onEach {
                when (it) {
                    HomeTab.Posts -> if (postsStateHolder.items.value is UIState.Empty) postsStateHolder.loadItems()
                    HomeTab.Comments -> if (commentsStateHolder.items.value is UIState.Empty) commentsStateHolder.loadItems()
                }
            }
            .launchIn(scope)

        scope.launch {
            postsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { post -> selectItemId(HomeTab.Posts, post.id) }
        }

        scope.launch {
            commentsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { comment -> selectItemId(HomeTab.Comments, comment.postId) }
        }
    }

    fun selectTab(tab: HomeTab) {
        mutableSelectedTab.value = tab
    }

    companion object {
        val Instance = HomeScreenStateHolder()
    }

    fun selectItemId(tab: HomeTab, id: String) {
        mutableSelectedItemIds.value += tab to id
    }

}
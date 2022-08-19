package com.rainbow.desktop.home

import com.rainbow.data.Repos
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Comment
import com.rainbow.domain.models.HomePostSorting
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.CommentRepository
import com.rainbow.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeScreenStateHolder private constructor(
    private val postRepository: PostRepository = Repos.Post,
    private val commentRepository: CommentRepository = Repos.Comment,
) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(HomeTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

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

    val commentsStateHolder = object : HomeScreenCommentsStateHolder(commentRepository.comments) {
        override suspend fun getItems(lastItem: Comment?): Result<Unit> {
            return commentRepository.getHomeComments(lastItem?.id)
        }
    }

    init {
        selectedTab
            .onEach {
                when (it) {
                    HomeTab.Posts -> if (postsStateHolder.items.value is UIState.Empty) postsStateHolder.loadItems()
                    HomeTab.Comments -> if (commentsStateHolder.items.value is UIState.Empty) commentsStateHolder.loadItems()
                }
            }
            .launchIn(scope)
    }

    fun selectTab(tab: HomeTab) {
        mutableSelectedTab.value = tab
    }

    companion object {
        private var stateHolder: HomeScreenStateHolder? = null

        fun getInstance(): HomeScreenStateHolder {
            return stateHolder ?: HomeScreenStateHolder().also {
                stateHolder = it
            }
        }
    }

}
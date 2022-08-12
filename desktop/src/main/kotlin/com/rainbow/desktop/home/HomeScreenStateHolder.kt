package com.rainbow.desktop.home

import com.rainbow.data.Repos
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.domain.models.HomePostSorting
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeScreenStateHolder(
    private val postRepository: PostRepository = Repos.Post,
) : StateHolder() {

    private val mutableSelectedTab = MutableStateFlow(HomeTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    val postsStateHolder = object : PostsStateHolder<HomePostSorting>(
        HomePostSorting.Default,
        postRepository.posts,
    ) {
        override suspend fun loadItems(
            sorting: HomePostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.getHomePosts(sorting, timeSorting, lastItem?.id)
    }

    init {

        //        selectedTab
//            .onEach {
//                when (it) {
//                    HomeTab.Posts -> if (!posts.value.isLoading) postListModel.loadItems()
//                    HomeTab.Comments -> if (commentListModel.items.value.isLoading) commentListModel.loadItems()
//                }
//            }
//            .launchIn(scope)
    }

    fun selectTab(tab: HomeTab) {
        mutableSelectedTab.value = tab
    }

}
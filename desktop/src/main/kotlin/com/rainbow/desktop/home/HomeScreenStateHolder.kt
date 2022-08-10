package com.rainbow.desktop.home

import com.rainbow.data.Repos
import com.rainbow.desktop.model.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.map
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.HomePostSorting
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class HomeScreenStateHolder(
    private val postRepository: PostRepository = Repos.Post,
) : StateHolder() {

    private val mutablePosts = MutableStateFlow<UIState<List<Post>>>(UIState.Empty)
    val posts get() = mutablePosts.asStateFlow()

    private val mutablePostSorting = MutableStateFlow(HomePostSorting.Default)
    val postSorting get() = mutablePostSorting.asStateFlow()

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    private val mutableSelectedTab = MutableStateFlow(HomeTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    init {
        combine(
            postSorting,
            timeSorting,
        ) { postSorting, timeSorting ->
            loadPosts(postSorting, timeSorting)
        }.launchIn(scope)
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

    private fun loadPosts(postSorting: HomePostSorting, timeSorting: TimeSorting) = scope.launch {
        mutablePosts.value = UIState.Loading
        mutablePosts.value = postRepository.getHomePosts(
            postSorting,
            timeSorting,
            null,
        ).toUIState()
    }


    fun setPostSorting(postSorting: HomePostSorting) {
        mutablePostSorting.value = postSorting
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        mutableTimeSorting.value = timeSorting
    }

    fun updatePost(post: Post) {
        mutablePosts.value = posts.value.map {
            it.map { currentPost ->
                if (currentPost.id == post.id) post else currentPost
            }
        }
    }
}
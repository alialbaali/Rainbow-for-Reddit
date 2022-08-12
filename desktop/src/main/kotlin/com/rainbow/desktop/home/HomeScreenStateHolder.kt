package com.rainbow.desktop.home

import com.rainbow.data.Repos
import com.rainbow.desktop.model.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrNull
import com.rainbow.domain.models.HomePostSorting
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.PostRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*

class HomeScreenStateHolder(
    private val postRepository: PostRepository = Repos.Post,
) : StateHolder() {

    private val mutablePosts = MutableStateFlow<UIState<List<Post>>>(UIState.Empty)
    val posts get() = mutablePosts.asStateFlow()
    private val currentPosts get() = posts.value.getOrNull()

    private val mutablePostSorting = MutableStateFlow(HomePostSorting.Default)
    val postSorting get() = mutablePostSorting.asStateFlow()

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    private val mutableSelectedTab = MutableStateFlow(HomeTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val mutableLastPostId = MutableStateFlow<String?>(null)

    private var previousJob: Job? = null

    init {
        combine(
            postSorting,
            timeSorting,
        ) { postSorting, timeSorting ->
            previousJob?.cancel()
            mutableLastPostId.value = null
            mutablePosts.value = UIState.Loading()
            postRepository.getHomePosts(postSorting, timeSorting, lastPostId = null)
                .onSuccess {
                    previousJob = postRepository.posts
                        .map { UIState.Success(it) }
                        .onEach { mutablePosts.value = it }
                        .launchIn(scope)
                }
                .onFailure { mutablePosts.value = UIState.Failure(null, it) }
        }.launchIn(scope)

        mutableLastPostId
            .filterNotNull()
            .onEach { lastPostId ->
                mutablePosts.value = UIState.Loading(currentPosts)
                postRepository.getHomePosts(postSorting.value, timeSorting.value, lastPostId)
                    .onFailure { mutablePosts.value = UIState.Failure(currentPosts, it) }
            }
            .launchIn(scope)

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

    fun setLastPostId(postId: String) {
        mutableLastPostId.value = postId
    }

    fun setPostSorting(postSorting: HomePostSorting) {
        mutablePostSorting.value = postSorting
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        mutableTimeSorting.value = timeSorting
    }

}
package com.rainbow.desktop.home

import com.rainbow.data.Repos
import com.rainbow.desktop.model.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.toUIState
import com.rainbow.domain.models.HomePostSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.PostRepository
import kotlinx.coroutines.flow.*

class HomeScreenStateHolder(
    private val postRepository: PostRepository = Repos.Post,
) : StateHolder() {

    val posts = postRepository.posts.stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val mutablePostSorting = MutableStateFlow(HomePostSorting.Default)
    val postSorting get() = mutablePostSorting.asStateFlow()

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    private val mutableSelectedTab = MutableStateFlow(HomeTab.Default)
    val selectedTab get() = mutableSelectedTab.asStateFlow()

    private val mutableLastPostId = MutableStateFlow<String?>(null)

    private val mutableState = MutableStateFlow<UIState<Unit>>(UIState.Empty)
    val state get() = mutableState.asStateFlow()

    init {
        combine(
            postSorting,
            timeSorting,
        ) { postSorting, timeSorting ->
            mutableLastPostId.value = null
            mutableState.value = UIState.Loading()
            mutableState.value = postRepository.getHomePosts(postSorting, timeSorting, lastPostId = null)
                .toUIState()
        }.launchIn(scope)

        mutableLastPostId
            .filterNotNull()
            .onEach { lastPostId ->
                mutableState.value = UIState.Loading()
                mutableState.value = postRepository.getHomePosts(postSorting.value, timeSorting.value, lastPostId)
                    .toUIState()
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
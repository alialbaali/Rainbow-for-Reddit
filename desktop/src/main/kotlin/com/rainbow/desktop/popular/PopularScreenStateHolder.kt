package com.rainbow.desktop.popular

import com.rainbow.data.Repos
import com.rainbow.desktop.post.PostsStateHolder
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.getOrNull
import com.rainbow.domain.models.PopularPostSorting
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostLayout
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.PostRepository
import com.rainbow.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PopularScreenStateHolder private constructor(
    private val postRepository: PostRepository = Repos.Post,
    private val settingsRepository: SettingsRepository = Repos.Settings,
) : StateHolder() {

    val postsStateHolder = object : PostsStateHolder<PopularPostSorting>(
        PopularPostSorting.Default,
        postRepository.popularPosts,
    ) {
        override suspend fun getItems(
            sorting: PopularPostSorting,
            timeSorting: TimeSorting,
            lastItem: Post?
        ): Result<Unit> = postRepository.getPopularPosts(sorting, timeSorting, lastItem?.id)
    }

    val postLayout = settingsRepository.postLayout.stateIn(scope, SharingStarted.Eagerly, PostLayout.Default)

    private val mutableSelectedItemId = MutableStateFlow<String?>(null)
    val selectedItemId get() = mutableSelectedItemId.asStateFlow()

    init {
        if (postsStateHolder.items.value is UIState.Empty) postsStateHolder.loadItems()

        scope.launch {
            postsStateHolder.items
                .firstOrNull { it.isSuccess }
                ?.getOrNull()
                ?.firstOrNull()
                ?.let { post -> selectItemId(post.id) }
        }
    }

    companion object {
        val Instance = PopularScreenStateHolder()
    }

    fun selectItemId(id: String) {
        mutableSelectedItemId.value = id
    }

}
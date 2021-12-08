package com.rainbow.app.post

import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PostModel<T : PostSorting>(
    initialPostSorting: T,
    private val getPosts: suspend (T, TimeSorting, String?) -> Result<List<Post>>,
) : Model() {

    private val mutablePosts = MutableStateFlow<UIState<Map<Post, Boolean>>>(UIState.Loading)
    val posts
        get() = mutablePosts.map {
            it.map {
                it.filterKeys { !it.isHidden }.map { it.key }
            }
        }.stateIn(scope, SharingStarted.Lazily, UIState.Loading)
    val selectedPost
        get() = mutablePosts.map state@{
            it.map {
                it.entries.firstOrNull {
                    it.value
                }?.key ?: return@state UIState.Empty
            }
        }.stateIn(scope, SharingStarted.Lazily, UIState.Loading)

    private val mutableTimeSorting = MutableStateFlow(TimeSorting.Default)
    val timeSorting get() = mutableTimeSorting.asStateFlow()

    private val mutableLastPost = MutableStateFlow<Post?>(null)
    val lastPost get() = mutableLastPost.asStateFlow()

    private val mutablePostSorting = MutableStateFlow(initialPostSorting)
    val postSorting get() = mutablePostSorting.asStateFlow()

    val postLayout = Repos.Settings.postLayout.stateIn(scope, SharingStarted.Lazily, PostLayout.Card)

    fun selectPost(postId: String) = scope.launch {
        mutablePosts.value = mutablePosts.value.map {
            it.mapValues {
                it.key.id == postId
            }
        }
    }

    fun loadPosts() {
        scope.launch {
            if (lastPost.value == null) mutablePosts.value = UIState.Loading
            mutablePosts.value = getPosts(postSorting.value, timeSorting.value, lastPost.value?.id)
                .map { it.associateWith { false } }
                .map {
                    if (selectedPost.value.isLoading)
                        it.entries.mapIndexed { index, entry ->
                            if (index == 0)
                                entry.key to true
                            else
                                entry.key to entry.value
                        }.toMap()
                    else
                        it
                }
                .map {
                    if (lastPost.value != null)
                        mutablePosts.value.getOrDefault(emptyMap()) + it
                    else
                        it
                }
                .toUIState()
        }
    }

    fun setTimeSorting(timeSorting: TimeSorting) {
        mutableTimeSorting.value = timeSorting
        mutableLastPost.value = null
        loadPosts()
    }

    fun setPostSorting(postSorting: T) {
        mutablePostSorting.value = postSorting
        mutableLastPost.value = null
        loadPosts()
    }

    fun setLastPost(lastPost: Post?) {
        mutableLastPost.value = lastPost
        loadPosts()
    }

    fun upvotePost(postId: String) = scope.launch {
        Repos.Post.upvotePost(postId).onSuccess {
            mutablePosts.value = mutablePosts.value.map {
                it.mapKeys { entry ->
                    if (entry.key.id == postId) entry.key.copy(vote = Vote.Up)
                    else entry.key
                }
            }
        }
    }

    fun downvotePost(postId: String) = scope.launch {
        Repos.Post.downvotePost(postId).onSuccess {
            mutablePosts.value = mutablePosts.value.map {
                it.mapKeys { entry ->
                    if (entry.key.id == postId) entry.key.copy(vote = Vote.Down)
                    else entry.key
                }
            }
        }
    }

    fun unvotePost(postId: String) = scope.launch {
        Repos.Post.unvotePost(postId).onSuccess {
            mutablePosts.value = mutablePosts.value.map {
                it.mapKeys { entry ->
                    if (entry.key.id == postId) entry.key.copy(vote = Vote.None)
                    else entry.key
                }
            }
        }
    }

    fun hidePost(postId: String) = scope.launch {
        Repos.Post.hidePost(postId)
            .onSuccess {
                mutablePosts.value = mutablePosts.value.map {
                    it.mapKeys {
                        it.key.copy(isHidden = if (it.key.id == postId) true else it.key.isHidden)
                    }
                }
            }
    }

    fun unHidePost(postId: String) = scope.launch {
        Repos.Post.unHidePost(postId)
            .onSuccess {
                mutablePosts.value = mutablePosts.value.map {
                    it.mapKeys {
                        it.key.copy(isHidden = if (it.key.id == postId) false else it.key.isHidden)
                    }
                }
            }
    }
}
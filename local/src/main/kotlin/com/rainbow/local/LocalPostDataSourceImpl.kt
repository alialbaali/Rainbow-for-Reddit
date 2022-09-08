package com.rainbow.local

import com.rainbow.domain.models.Post
import com.rainbow.domain.models.Vote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

class LocalPostDataSourceImpl : LocalPostDataSource {

    private val mutablePosts = MutableStateFlow(emptyList<Post>())
    override val posts get() = mutablePosts.asStateFlow()

    private val mutableHomePosts = MutableStateFlow(emptyList<Post>())
    override val homePosts get() = mutableHomePosts.asStateFlow()

    private val mutableProfileSubmittedPosts = MutableStateFlow(emptyList<Post>())
    override val profileSubmittedPosts get() = mutableProfileSubmittedPosts.asStateFlow()

    private val mutableProfileUpvotedPosts = MutableStateFlow(emptyList<Post>())
    override val profileUpvotedPosts get() = mutableProfileUpvotedPosts.asStateFlow()

    private val mutableProfileDownvotedPosts = MutableStateFlow(emptyList<Post>())
    override val profileDownvotedPosts get() = mutableProfileDownvotedPosts.asStateFlow()

    private val mutableProfileHiddenPosts = MutableStateFlow(emptyList<Post>())
    override val profileHiddenPosts get() = mutableProfileHiddenPosts.asStateFlow()

    private val mutableUserSubmittedPosts = MutableStateFlow(emptyList<Post>())
    override val userSubmittedPosts get() = mutableUserSubmittedPosts.asStateFlow()

    private val mutableSubredditPosts = MutableStateFlow(emptyList<Post>())
    override val subredditPosts get() = mutableSubredditPosts.asStateFlow()

    private val mutableSearchPosts = MutableStateFlow(emptyList<Post>())
    override val searchPosts get() = mutableSearchPosts.asStateFlow()

    private val allPosts = listOf(
        mutablePosts,
        mutableHomePosts,
        mutableProfileSubmittedPosts,
        mutableProfileUpvotedPosts,
        mutableProfileDownvotedPosts,
        mutableProfileHiddenPosts,
        mutableUserSubmittedPosts,
        mutableSubredditPosts,
        mutableSearchPosts,
    )

    override fun insertPost(post: Post) {
        mutablePosts.value = posts.value + post
    }

    override fun insertHomePost(post: Post) {
        mutableHomePosts.value = homePosts.value + post
    }

    override fun insertProfileSubmittedPost(post: Post) {
        mutableProfileSubmittedPosts.value = profileSubmittedPosts.value + post
    }

    override fun insertProfileUpvotedPost(post: Post) {
        mutableProfileUpvotedPosts.value = profileUpvotedPosts.value + post
    }

    override fun insertProfileDownvotedPost(post: Post) {
        mutableProfileDownvotedPosts.value = profileDownvotedPosts.value + post
    }

    override fun insertProfileHiddenPost(post: Post) {
        mutableProfileHiddenPosts.value = profileHiddenPosts.value + post
    }

    override fun insertUserSubmittedPost(post: Post) {
        mutableUserSubmittedPosts.value = userSubmittedPosts.value + post
    }

    override fun insertSubredditPost(post: Post) {
        mutableSubredditPosts.value = subredditPosts.value + post
    }

    override fun insertSearchPost(post: Post) {
        mutableSearchPosts.value = searchPosts.value + post
    }

    override fun getPost(postId: String): Flow<Post?> {
        return combine(allPosts) { arrayOfPosts ->
            arrayOfPosts.toList()
                .flatten()
                .find { post -> post.id == postId }
        }
    }

    override fun updatePost(postId: String, block: (Post) -> Post) {
        allPosts.forEach { state ->
            state.value = state.value.map { post ->
                if (post.id == postId)
                    block(post)
                else
                    post
            }
        }
    }

    override fun upvotePost(postId: String) {
        allPosts.forEach { state ->
            state.value = state.value.map { post ->
                if (post.id == postId)
                    post.copy(vote = Vote.Up)
                else
                    post
            }
        }
    }

    override fun downvotePost(postId: String) {
        allPosts.forEach { state ->
            state.value = state.value.map { post ->
                if (post.id == postId)
                    post.copy(vote = Vote.Down)
                else
                    post
            }
        }
    }

    override fun unvotePost(postId: String) {
        allPosts.forEach { state ->
            state.value = state.value.map { post ->
                if (post.id == postId)
                    post.copy(vote = Vote.None)
                else
                    post
            }
        }
    }

    override fun clearPosts() {
        mutablePosts.value = emptyList()
    }

    override fun clearHomePosts() {
        mutableHomePosts.value = emptyList()
    }

    override fun clearProfileSubmittedPosts() {
        mutableProfileSubmittedPosts.value = emptyList()
    }

    override fun clearProfileUpvotedPosts() {
        mutableProfileUpvotedPosts.value = emptyList()
    }

    override fun clearProfileDownvotedPosts() {
        mutableProfileDownvotedPosts.value = emptyList()
    }

    override fun clearProfileHiddenPosts() {
        mutableProfileHiddenPosts.value = emptyList()
    }

    override fun clearUserSubmittedPosts() {
        mutableUserSubmittedPosts.value = emptyList()
    }

    override fun clearSubredditPosts() {
        mutableSubredditPosts.value = emptyList()
    }

    override fun clearSearchPosts() {
        mutableSearchPosts.value = emptyList()
    }

}
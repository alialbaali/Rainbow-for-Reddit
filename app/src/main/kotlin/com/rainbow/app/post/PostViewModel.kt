package com.rainbow.app.post

import com.rainbow.app.post.PostViewModel.PostIntent
import com.rainbow.app.post.PostViewModel.PostState
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.ViewModel
import com.rainbow.domain.models.Post
import com.rainbow.domain.models.PostListSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class PostViewModel(
    private val postRepository: PostRepository = Repos.Post,
) : ViewModel<PostIntent, PostState>(PostState()) {

    override suspend fun onIntent(intent: PostIntent): PostState = when (intent) {
        is PostIntent.GetSubredditPosts -> withState {
            copy(
//                posts = postRepository
//                    .getSubredditPosts(intent.subredditName, intent.postsSorting, intent.timeSorting, intent.lastPostId)
//                    .toUIState(),
            )
        }
        is PostIntent.GetUserPosts -> withState {
            copy(
//                posts = postRepository
//                    .getUserPosts(intent.userName, intent.postsSorting, intent.timeSorting, intent.lastPostId)
//                    .toUIState(),
            )
        }
        is PostIntent.GetHomePosts -> withState {
            copy(
                posts = postRepository
                    .getHomePosts(intent.postsSorting, intent.timeSorting, intent.lastPostId)
                    .map { it.toUIState() }
            )
        }
        is PostIntent.Upvote -> withState {
            postRepository.upvotePost(intent.postId)
            this
        }

        is PostIntent.Unvote -> withState {
            postRepository.unvotePost(intent.postId)
            this
        }

        is PostIntent.Downvote -> withState {
            postRepository.downvotePost(intent.postId)
            this
        }

        is PostIntent.SelectPost -> withState {
            copy(
//                selectedPost = postRepository.getPost(intent.postId)
//                    .toUIState()
            )
        }
    }

    sealed class PostIntent : Intent {

        data class GetSubredditPosts(
            val subredditName: String,
            val postsSorting: PostListSorting,
            val timeSorting: TimeSorting,
            val lastPostId: String?,
        ) : PostIntent()

        data class GetUserPosts(
            val userName: String,
            val postsSorting: PostListSorting,
            val timeSorting: TimeSorting,
            val lastPostId: String?,
        ) : PostIntent()

        data class GetHomePosts(
            val postsSorting: PostListSorting,
            val timeSorting: TimeSorting,
            val lastPostId: String?,
        ) : PostIntent()

        data class SelectPost(val postId: String) : PostIntent()

        data class Upvote(val postId: String) : PostIntent()

        data class Unvote(val postId: String) : PostIntent()

        data class Downvote(val postId: String) : PostIntent()

    }

    data class PostState(
        val posts: Flow<UIState<List<Post>>> = emptyFlow(),
        val selectedPost: UIState<Post> = UIState.Loading,
    ) : State

}


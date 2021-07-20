package com.rainbow.app.comment

import com.rainbow.app.comment.CommentViewModel.CommentIntent
import com.rainbow.app.comment.CommentViewModel.CommentState
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.withState
import com.rainbow.data.Repos
import com.rainbow.domain.ViewModel
import com.rainbow.domain.models.Comment
import com.rainbow.domain.repository.CommentRepository

class CommentViewModel(
    private val commentRepository: CommentRepository = Repos.Comment,
) : ViewModel<CommentIntent, CommentState>(CommentState()) {

    override suspend fun onIntent(intent: CommentIntent): CommentState = when (intent) {
        is CommentIntent.GetPostComments -> withState {
            this
        //            copy(
//                comments = commentRepository.getPostsComments(intent.postId)
//                    .toUIState()
//            )
        }
        is CommentIntent.Upvote -> withState {
            this
        }
        is CommentIntent.Downvote -> TODO()
        is CommentIntent.Unvote -> TODO()
    }

    sealed class CommentIntent : Intent {

        data class GetPostComments(val postId: String) : CommentIntent()

        data class Upvote(val commentId: String) : CommentIntent()

        data class Downvote(val commentId: String) : CommentIntent()

        data class Unvote(val commentId: String) : CommentIntent()

    }

    data class CommentState(
        val comments: UIState<List<Comment>> = UIState.Loading,
    ) : State

}
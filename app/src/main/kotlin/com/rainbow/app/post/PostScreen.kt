package com.rainbow.app.post

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.app.comment.CommentContent
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.composed
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Composable
fun PostScreen(postId: String, modifier: Modifier = Modifier) {

    val scope = rememberCoroutineScope()

    val state by produceState<UIState<Post>>(UIState.Loading, postId) {
        Repos.Post.getPost(postId)
            .map { it.toUIState() }
            .collect { value = it }
    }

    state.composed { post ->

        CommentContent(postId, modifier) {

            Post(
                post,
                onUpvote = { scope.launch { Repos.Post.upvotePost(postId) } },
                onDownvote = { scope.launch { Repos.Post.downvotePost(postId) } },
                onUnvote = { scope.launch { Repos.Post.unvotePost(postId) } },
                onShare = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )

        }
    }
}

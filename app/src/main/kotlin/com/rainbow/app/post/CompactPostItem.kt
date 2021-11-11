package com.rainbow.app.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.ShapeModifier
import com.rainbow.app.utils.defaultPadding
import com.rainbow.domain.models.Post

@Composable
inline fun CompactPostItem(
    post: Post,
    crossinline onClick: (Post) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isTextPost = remember(post) { post.type is Post.Type.Text }
    Column(
        modifier
            .padding(vertical = 8.dp)
            .then(ShapeModifier)
            .clickable { onClick(post) }
            .defaultPadding(),
    ) {
        PostInfo(
            post = post,
            onUserNameClick,
            onSubredditNameClick,
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
        )
        if (isTextPost) {
            PostTitle(
                title = post.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
            PostContent(
                post = post,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        } else {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                PostTitle(
                    title = post.title,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                )
                PostContent(
                    post = post,
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(100.dp)
                )
            }
        }
        PostCommands(
            post,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}
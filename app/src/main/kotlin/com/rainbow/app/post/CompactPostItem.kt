package com.rainbow.app.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    Column(
        modifier
            .padding(vertical = 8.dp)
            .then(ShapeModifier)
            .clickable { onClick(post) }
            .defaultPadding(),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Column {
                PostInfo(
                    post = post,
                    onUserNameClick,
                    onSubredditNameClick,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                )

                PostTitle(
                    title = post.title,
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                )
            }
            PostContent(
                post = post,
                modifier = Modifier
                    .wrapContentWidth(Alignment.End)
                    .height(100.dp)
            )
        }
        PostCommands(
            post,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

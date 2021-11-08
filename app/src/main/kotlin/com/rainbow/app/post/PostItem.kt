package com.rainbow.app.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.ShapeModifier
import com.rainbow.app.utils.defaultPadding
import com.rainbow.data.Repos
import com.rainbow.domain.models.Post

@Composable
inline fun PostItem(
    post: Post,
    crossinline onClick: (Post) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    val isFullHeight by Repos.Settings.isFullHeight.collectAsState(false)

    Column(
        modifier
            .padding(vertical = 8.dp)
            .then(ShapeModifier)
            .clickable { onClick(post) }
            .defaultPadding(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        PostInfo(
            post = post,
            onUserNameClick = onUserNameClick,
            onSubredditNameClick = onSubredditNameClick,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Spacer(Modifier.height(8.dp))

        PostTitle(
            title = post.title,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        Spacer(Modifier.height(8.dp))

        PostContent(
            post = post,
            modifier = Modifier
                .then(
                    if (isFullHeight)
                        Modifier.fillMaxHeight()
                    else
                        Modifier.heightIn(max = 500.dp)
                )
                .fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        PostCommands(
            post,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}
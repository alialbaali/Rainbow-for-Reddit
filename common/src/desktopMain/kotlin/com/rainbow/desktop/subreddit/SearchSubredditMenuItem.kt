package com.rainbow.desktop.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.domain.models.Subreddit

private val ImageModifier
    @Composable
    get() = Modifier.size(24.dp).clip(MaterialTheme.shapes.small).background(MaterialTheme.colorScheme.secondary)

@Composable
fun SearchSubredditMenuItem(
    subreddit: Subreddit,
    onSubredditClick: (Subreddit) -> Unit,
    modifier: Modifier = Modifier,
) {
//    DropdownMenuItem(
//        onClick = { onSubredditClick(subreddit) },
//        modifier,
//    ) {
//        val painterResource = lazyPainterResource(subreddit.imageUrl ?: subreddit.bannerImageUrl.toString())
//        KamelImage(
//            painterResource,
//            subreddit.name,
//            ImageModifier,
//            onLoading = { RainbowProgressIndicator(Modifier.size(24.dp)) },
//            onFailure = { TextBox(subreddit.name, 16.sp, ImageModifier) }
//        )
//        Spacer(Modifier.width(16.dp))
//        Text(subreddit.name)
//    }
}
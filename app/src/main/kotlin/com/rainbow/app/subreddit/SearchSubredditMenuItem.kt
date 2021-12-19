package com.rainbow.app.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.components.TextBox
import com.rainbow.domain.models.Subreddit
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

private val ImageModifier
    @Composable
    get() = Modifier.size(24.dp).clip(MaterialTheme.shapes.small).background(MaterialTheme.colors.secondary)

@Composable
fun SearchSubredditMenuItem(
    subreddit: Subreddit,
    onSubredditClick: (Subreddit) -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownMenuItem(
        onClick = { onSubredditClick(subreddit) },
        modifier,
    ) {
        val painterResource = lazyPainterResource(subreddit.imageUrl ?: subreddit.bannerImageUrl.toString())
        KamelImage(
            painterResource,
            subreddit.name,
            ImageModifier,
            onLoading = { RainbowProgressIndicator(Modifier.size(24.dp)) },
            onFailure = { TextBox(subreddit.name, 16.sp, ImageModifier) }
        )
        Spacer(Modifier.width(16.dp))
        Text(subreddit.name)
    }
}
package com.rainbow.app.sidebar

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rainbow.app.subreddit.SubredditViewModel
import com.rainbow.app.ui.dimensions
import com.rainbow.app.utils.*
import com.rainbow.domain.models.Subreddit
import io.kamel.core.LazyImage
import io.kamel.core.lazyImageResource
import io.ktor.http.*

private inline val DefaultHeaderTextStyle
    @Composable
    get() = MaterialTheme.typography.h6

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun SidebarSubredditList(
    onClick: (String) -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
    itemModifier: (Subreddit) -> Modifier = { Modifier },
) {

    val scrollState = rememberLazyListState()

    val (state, emitIntent) = remember { SubredditViewModel() }

    remember(null) { emitIntent(SubredditViewModel.SubredditIntent.GetMySubreddits) }

    val (favoritedSubreddits, unFavoriteSubreddits) = state.mySubreddits
        .getOrDefault(emptyList())
        .partition { subreddit -> subreddit.isFavorite }

    val subredditItems: LazyListScope.(List<Subreddit>) -> Unit = { subreddits ->
        items(subreddits) { subreddit ->
            SubredditItem(
                subreddit,
                onClick = {
                    onClick(it)
                    emitIntent(SubredditViewModel.SubredditIntent.GetSubreddit(subreddit.name))
                },
                onFavorite = {},
                isExpanded,
                modifier = itemModifier(subreddit)
                    .fillParentMaxWidth()
                    .wrapContentHeight()
            )
        }
    }

    LazyColumn(
        modifier = modifier,
        scrollState,
    ) {

        if (isExpanded)
            stickyHeader {
                Text(
                    text = RainbowStrings.Favorites,
                    modifier = Modifier.fillParentMaxWidth(),
                    style = DefaultHeaderTextStyle
                )
            }
        else
            stickyDivider(color = Color.Blue)

        subredditItems(favoritedSubreddits)

        if (isExpanded)
            stickyHeader {
                Text(
                    text = RainbowStrings.Subscriptions,
                    modifier = Modifier.fillParentMaxWidth(),
                    style = DefaultHeaderTextStyle
                )
            }
        else
            stickyDivider(color = Color.Red)

        subredditItems(unFavoriteSubreddits)

    }
}

@Composable
private fun SubredditItem(
    subreddit: Subreddit,
    onClick: (String) -> Unit,
    onFavorite: (Boolean) -> Unit,
    isExpanded: Boolean,
    modifier: Modifier = Modifier,
) {

    Row(
        modifier = modifier
            .clickable { onClick(subreddit.name) }
            .padding(MaterialTheme.dimensions.medium)
            .layoutId(subreddit.id),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.medium),
    ) {

        if (!subreddit.imageUrl.isNullOrBlank()) {
            LazyImage(
                resource = lazyImageResource(subreddit.imageUrl.toString()),
                contentDescription = null,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .size(24.dp)
                    .weight(1F),
            )
        } else {
            Box(
                modifier = Modifier
                    .background(Color.Green, CircleShape)
                    .size(24.dp)
                    .weight(1F)
            )
        }

        if (isExpanded) {
            Text(
                text = subreddit.name,
                style = MaterialTheme.typography.subtitle2,
                modifier = Modifier
                    .weight(2F)
            )

            IconToggleButton(
                checked = subreddit.isFavorite,
                onCheckedChange = {

                },
                modifier = Modifier
                    .weight(1F)
            ) {
                if (subreddit.isFavorite)
                    Icon(RainbowIcons.Star, tint = Color.Yellow, contentDescription = null)
                else
                    Icon(RainbowIcons.StarBorder, tint = Color.LightGray, contentDescription = null )
            }

        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.stickyDivider(
    modifier: Modifier = Modifier,
    color: Color = Color.Black,
    thickness: Dp = 2.dp,
) {
    stickyHeader {
        Divider(
            modifier
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.1F))
                .shadow(10.dp)
                .fillParentMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            color,
            thickness
        )
    }
}
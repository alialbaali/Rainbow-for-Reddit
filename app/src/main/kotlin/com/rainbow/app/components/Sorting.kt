package com.rainbow.app.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.defaultSurfaceShape
import com.rainbow.domain.models.*

@Composable
inline fun <reified T : Enum<T>> PostSorting(
    postsSorting: T,
    crossinline onSortingUpdate: (T) -> Unit,
    timeSorting: TimeSorting,
    crossinline onTimeSortingUpdate: (TimeSorting) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Sorting(
            sorting = postsSorting,
            onSortingUpdate = onSortingUpdate
        )

        if (postsSorting is PostSorting && postsSorting.isTimeSorting)
            Sorting(
                sorting = timeSorting,
                onSortingUpdate = onTimeSortingUpdate
            )
    }
}

@Composable
inline fun <reified T : Enum<T>> Sorting(
    sorting: T,
    crossinline onSortingUpdate: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    val iconRotation by animateFloatAsState(if (isMenuVisible) 180F else 0F)
    val values = remember(sorting) { enumValues<T>() }
    Column(modifier) {
        Row(
            Modifier
                .defaultSurfaceShape(shape = MaterialTheme.shapes.medium)
                .clickable { isMenuVisible = !isMenuVisible }
                .defaultPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                sorting.name,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.primary,
            )

            Icon(
                RainbowIcons.ArrowDropUp,
                contentDescription = null,
                modifier = Modifier
                    .rotate(iconRotation),
                tint = MaterialTheme.colors.primary,
            )
        }

        DropdownMenu(
            isMenuVisible,
            onDismissRequest = { isMenuVisible = !isMenuVisible },
        ) {
            values.forEach { sorting ->
                DropdownMenuItem(
                    onClick = {
                        onSortingUpdate(sorting)
                        isMenuVisible = false
                    },
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    (sorting as? Sorting)?.icon?.let { icon ->
                        Icon(icon, sorting.name)
                    }
                    Spacer(Modifier.width(16.dp))
                    Text(text = sorting.name, Modifier.fillMaxWidth())
                }
            }
        }
    }
}

val Sorting.icon
    get() = when (this) {
        is UserPostSorting -> when (this) {
            UserPostSorting.Hot -> RainbowIcons.Whatshot
            UserPostSorting.New -> RainbowIcons.BrightnessLow
            UserPostSorting.Top -> RainbowIcons.BarChart
            UserPostSorting.Controversial -> RainbowIcons.TrendingDown
        }
        is SubredditPostSorting -> when (this) {
            SubredditPostSorting.Hot -> RainbowIcons.Whatshot
            SubredditPostSorting.Top -> RainbowIcons.BarChart
            SubredditPostSorting.Controversial -> RainbowIcons.TrendingDown
            SubredditPostSorting.Rising -> RainbowIcons.TrendingUp
        }
        is HomePostSorting -> when (this) {
            HomePostSorting.Best -> RainbowIcons.Star
            HomePostSorting.New -> RainbowIcons.BrightnessLow
            HomePostSorting.Controversial -> RainbowIcons.TrendingDown
            HomePostSorting.Top -> RainbowIcons.BarChart
            HomePostSorting.Hot -> RainbowIcons.Whatshot
            HomePostSorting.Rising -> RainbowIcons.TrendingUp
        }
        is SearchPostSorting -> when (this) {
            SearchPostSorting.Relevance -> RainbowIcons.Star
            SearchPostSorting.New -> RainbowIcons.BrightnessLow
            SearchPostSorting.Top -> RainbowIcons.BarChart
            SearchPostSorting.Hot -> RainbowIcons.Whatshot
            SearchPostSorting.CommentsCount -> RainbowIcons.TrendingUp
        }
        is PostCommentSorting -> when (this) {
            PostCommentSorting.Confidence -> RainbowIcons.Star
            PostCommentSorting.Top -> RainbowIcons.BarChart
            PostCommentSorting.Best -> RainbowIcons.Star
            PostCommentSorting.New -> RainbowIcons.BrightnessLow
            PostCommentSorting.Old -> RainbowIcons.BrightnessLow
            PostCommentSorting.Controversial -> RainbowIcons.TrendingDown
            PostCommentSorting.QA -> RainbowIcons.TrendingDown
        }
    }
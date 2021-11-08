package com.rainbow.app.post

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.defaultPadding
import com.rainbow.domain.models.MainPostSorting
import com.rainbow.domain.models.SubredditPostSorting
import com.rainbow.domain.models.TimeSorting
import com.rainbow.domain.models.UserPostSorting

val UserPostSorting.isTimeSorting get() = this == UserPostSorting.Top || this == UserPostSorting.Controversial
val SubredditPostSorting.isTimeSorting get() = this == SubredditPostSorting.Top || this == SubredditPostSorting.Controversial
val MainPostSorting.isTimeSorting get() = this == MainPostSorting.Top || this == MainPostSorting.Controversial

@Composable
inline fun <reified T : Enum<T>> Sorting(
    postsSorting: T,
    crossinline onSortingUpdate: (T) -> Unit,
    timeSorting: TimeSorting,
    crossinline onTimeSortingUpdate: (TimeSorting) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        PostsSorting(
            sorting = postsSorting,
            onSortingUpdate = onSortingUpdate
        )

        val isUserTimeSorting = (postsSorting is UserPostSorting && postsSorting.isTimeSorting)
        val isSubredditTimeSorting = (postsSorting is SubredditPostSorting && postsSorting.isTimeSorting)
        val isMainTimeSorting = (postsSorting is MainPostSorting && postsSorting.isTimeSorting)

        if (isUserTimeSorting || isSubredditTimeSorting || isMainTimeSorting)
            PostsSorting(
                sorting = timeSorting,
                onSortingUpdate = onTimeSortingUpdate
            )
    }
}

@Composable
inline fun <reified T : Enum<T>> PostsSorting(
    sorting: T,
    crossinline onSortingUpdate: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuVisible by remember { mutableStateOf(false) }
    val iconRotation by animateFloatAsState(if (isMenuVisible) 180F else 0F)
    Column(modifier) {
        OutlinedButton(
            onClick = { isMenuVisible = !isMenuVisible },
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(
                sorting.name,
                style = MaterialTheme.typography.subtitle1
            )

            Icon(
                RainbowIcons.ArrowDropUp,
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .rotate(iconRotation),
                tint = MaterialTheme.colors.primary,
            )
        }

        DropdownMenu(
            isMenuVisible,
            onDismissRequest = { isMenuVisible = !isMenuVisible },
        ) {
            enumValues<T>().forEach { sorting ->
                DropdownMenuItem(
                    onClick = {
                        onSortingUpdate(sorting)
                        isMenuVisible = false
                    },
                ) {
                    sorting.icon?.let { icon ->
                        Icon(icon, sorting.name)
                    }
                    Text(
                        text = sorting.name,
                        modifier = Modifier
                            .defaultPadding()
                            .wrapContentWidth()
                    )
                }
            }
        }
    }
}

val <T> T.icon
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
        is MainPostSorting -> when (this) {
            MainPostSorting.Best -> RainbowIcons.Star
            MainPostSorting.New -> RainbowIcons.BrightnessLow
            MainPostSorting.Controversial -> RainbowIcons.TrendingDown
            MainPostSorting.Top -> RainbowIcons.BarChart
            MainPostSorting.Hot -> RainbowIcons.Whatshot
            MainPostSorting.Rising -> RainbowIcons.TrendingUp
        }
        else -> null
    }
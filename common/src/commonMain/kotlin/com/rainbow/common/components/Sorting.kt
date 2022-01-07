package com.rainbow.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.domain.models.*

@Composable
inline fun <reified T : Enum<T>> Sorting(
    postsSorting: T,
    crossinline onSortingUpdate: (T) -> Unit,
    timeSorting: TimeSorting,
    crossinline onTimeSortingUpdate: (TimeSorting) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        DropdownMenuHolder(
            postsSorting,
            onSortingUpdate
        )

        if (postsSorting is PostSorting && postsSorting.isTimeSorting)
            DropdownMenuHolder(
                timeSorting,
                onTimeSortingUpdate
            )
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
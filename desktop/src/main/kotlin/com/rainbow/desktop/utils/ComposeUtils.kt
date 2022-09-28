package com.rainbow.desktop.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.domain.models.*

typealias RainbowIcons = Icons.Rounded

private const val PagingEffectIndexThreshold = 10 // Higher means faster paging.

@Composable
fun DefaultContentPadding() = PaddingValues(vertical = RainbowTheme.dimensions.medium)

@NonRestartableComposable
@Composable
fun PagingEffect(
    currentIndex: Int,
    lastIndex: Int,
    block: () -> Unit,
) {
    if (currentIndex == lastIndex - PagingEffectIndexThreshold) SideEffect(block)
}

@Composable
@NonRestartableComposable
fun OneTimeEffect(
    vararg keys: Any?,
    effect: DisposableEffectScope.() -> Unit,
) {
    DisposableEffect(*keys) {
        effect()
        onDispose {

        }
    }
}

fun Modifier.defaultPadding(
    start: Dp = 16.dp,
    top: Dp = 16.dp,
    end: Dp = 16.dp,
    bottom: Dp = 16.dp,
) = padding(start, top, end, bottom)

val Theme.text
    get() = when (this) {
        Theme.Dark -> name
        Theme.Light -> name
        Theme.System -> name
    }

val ItemSorting.icon
    get() = when (this) {
        is PostSorting -> when (this) {
            is UserPostSorting -> when (this) {
                UserPostSorting.Hot -> RainbowIcons.Whatshot
                UserPostSorting.New -> RainbowIcons.FiberNew
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
                HomePostSorting.New -> RainbowIcons.FiberNew
                HomePostSorting.Controversial -> RainbowIcons.TrendingDown
                HomePostSorting.Top -> RainbowIcons.BarChart
                HomePostSorting.Hot -> RainbowIcons.Whatshot
                HomePostSorting.Rising -> RainbowIcons.TrendingUp
            }

            is SearchPostSorting -> when (this) {
                SearchPostSorting.Relevance -> RainbowIcons.Star
                SearchPostSorting.New -> RainbowIcons.FiberNew
                SearchPostSorting.Top -> RainbowIcons.BarChart
                SearchPostSorting.Hot -> RainbowIcons.Whatshot
                SearchPostSorting.CommentsCount -> RainbowIcons.TrendingUp
            }
        }

        is PostCommentSorting -> when (this) {
            PostCommentSorting.Confidence -> RainbowIcons.DoneAll
            PostCommentSorting.Top -> RainbowIcons.BarChart
            PostCommentSorting.Best -> RainbowIcons.Star
            PostCommentSorting.New -> RainbowIcons.FiberNew
            PostCommentSorting.Old -> RainbowIcons.History
            PostCommentSorting.Controversial -> RainbowIcons.TrendingDown
            PostCommentSorting.QA -> RainbowIcons.QuestionAnswer
        }
    }

@Composable
inline fun <reified T : StateHolder> rememberStateHolder(crossinline factory: @DisallowComposableCalls () -> T): T {
    val stateHolder = remember { factory() }
    object : RememberObserver {
        override fun onAbandoned() {
//            stateHolder.scope.cancel()
        }

        override fun onForgotten() {
//            stateHolder.scope.cancel()
        }

        override fun onRemembered() {

        }
    }
    return stateHolder
}

private val stateHolders = mutableListOf<StateHolder>()

fun Long.toColor() = Color(this)
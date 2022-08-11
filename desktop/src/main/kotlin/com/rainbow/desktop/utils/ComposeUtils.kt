package com.rainbow.desktop.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.model.StateHolder
import com.rainbow.domain.models.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val RainbowIcons = Icons.Rounded

val ImageBorderSize = 6.dp

@NonRestartableComposable
@Composable
inline fun <T> PagingEffect(
    iterable: Iterable<T>,
    currentIndex: Int,
    crossinline block: (T) -> Unit,
) {
    SideEffect {
        iterable.runOnIndex(currentIndex, block)
    }
}

inline fun <T> Iterable<T>.runOnIndex(currentIndex: Int, block: (T) -> Unit) {
    withIndex().last().apply {
        if (index == currentIndex)
            block(value)
    }
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

@Composable
fun Modifier.defaultBackgroundShape(
    borderWidth: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.1F),
    shape: Shape = RoundedCornerShape(16.dp),
) = this.then(
    Modifier
        .border(borderWidth, borderColor, shape)
        .background(MaterialTheme.colorScheme.background, shape)
        .clip(shape)
)

@Composable
fun Modifier.defaultSurfaceShape(
    borderWidth: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.1F),
    shape: Shape = RoundedCornerShape(16.dp),
) = this.then(
    Modifier
        .border(borderWidth, borderColor, shape)
        .background(MaterialTheme.colorScheme.surface, shape)
        .clip(shape)
)

fun Modifier.defaultPadding(
    start: Dp = 16.dp,
    top: Dp = 16.dp,
    end: Dp = 16.dp,
    bottom: Dp = 16.dp,
) = padding(start, top, end, bottom)

val LocalDateTime.displayTime: String
    get() {
        val currentDateTime = LocalDateTime.now()
        return when (val dayDiff = currentDateTime.dayOfYear - dayOfYear) {
            0 -> when (val hourDiff = currentDateTime.hour - hour) {
                0 -> when (val minuteDiff = currentDateTime.minute - minute) {
                    0 -> "Now"
                    in 0..60 -> minuteDiff.toString().plus("m")
                    else -> error("Wrong Minute")
                }

                in 1..24 -> hourDiff.toString().plus("h")
                else -> error("Wrong Hour")
            }

            else -> dayDiff.toString().plus("d")
        }
    }

@Composable
inline fun <T> UIState<T>.composed(
    noinline onShowSnackbar: ((String) -> Unit)?,
    modifier: Modifier = Modifier,
    onLoading: @Composable () -> Unit = { RainbowProgressIndicator(modifier) },
    onFailure: @Composable (Throwable) -> Unit = { if (onShowSnackbar != null) onShowSnackbar(it.message.toString()) },
    onSuccess: @Composable (T) -> Unit,
) {
    when (this) {
        is UIState.Loading -> onLoading()
        is UIState.Success -> onSuccess(data)
        is UIState.Failure -> onFailure(exception)
        UIState.Empty -> {}
    }
}

fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

val Sorting.text
    get() = when (this) {
        else -> ""
    }

val Theme.text
    get() = when (this) {
        Theme.Dark -> name
        Theme.Light -> name
        Theme.System -> name
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

inline fun <reified T : StateHolder> rememberStateHolder(crossinline factory: () -> T): T {
    val stateHolder = factory()
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
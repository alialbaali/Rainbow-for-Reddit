package com.rainbow.app.utils

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.RainbowProgressIndicator
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
    crossinline block: (T) -> Unit
) {
    SideEffect {
        iterable.onIndex(currentIndex, block)
    }
}

inline fun <T> Iterable<T>.onIndex(currentIndex: Int, block: (T) -> Unit) {
    withIndex().last().apply {
        if (index == currentIndex)
            block(value)
    }
}

@Composable
@NonRestartableComposable
fun OneTimeEffect(
    key1: Any?,
    effect: DisposableEffectScope.() -> Unit
) {
    DisposableEffect(key1) {
        effect()
        onDispose {

        }
    }
}

val ShapeModifier
    @Composable
    get() = Modifier
        .border(1.dp, MaterialTheme.colors.onBackground.copy(0.1F), MaterialTheme.shapes.large)
        .background(MaterialTheme.colors.background, MaterialTheme.shapes.large)
        .clip(MaterialTheme.shapes.large)


fun Modifier.defaultPadding(
    start: Dp = 16.dp,
    top: Dp = 16.dp,
    end: Dp = 16.dp,
    bottom: Dp = 16.dp
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

var GraphicsLayerScope.shadow: Dp
    get() = shadowElevation.dp
    set(value) {
        shadowElevation = value.value
    }

@OptIn(ExperimentalAnimationApi::class)
@Composable
inline fun <T> UIState<T>.composed(
    modifier: Modifier = Modifier,
    actionLabel: String = "Hide",
    onEmpty: @Composable () -> Unit = {},
    onSuccess: @Composable (T) -> Unit,
) {
    when (this) {
        is UIState.Loading -> RainbowProgressIndicator(modifier)
        is UIState.Success -> onSuccess(value)
        is UIState.Failure -> {
            throw exception
            //            val snackbarHostState = remember { SnackbarHostState() }
//            SnackbarHost(snackbarHostState, modifier = Modifier.defaultPadding())
//            LaunchedEffect(Unit) {
//                snackbarHostState.showSnackbar(exception.message.toString(), actionLabel)
//            }
        }
        UIState.Empty -> onEmpty()
    }
}

fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun Modifier.handy() = border(5.dp, color = Color.Red)
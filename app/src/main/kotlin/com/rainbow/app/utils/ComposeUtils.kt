package com.rainbow.app.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
    borderColor: Color = MaterialTheme.colors.onBackground.copy(0.1F),
    shape: Shape = MaterialTheme.shapes.large,
) = this.then(
    Modifier
        .border(borderWidth, borderColor, shape)
        .background(MaterialTheme.colors.background, shape)
        .clip(shape)
)

@Composable
fun Modifier.defaultSurfaceShape(
    borderWidth: Dp = 1.dp,
    borderColor: Color = MaterialTheme.colors.onBackground.copy(0.1F),
    shape: Shape = MaterialTheme.shapes.large,
) = this.then(
    Modifier
        .border(borderWidth, borderColor, shape)
        .background(MaterialTheme.colors.surface, shape)
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
fun <T> UIState<T>.composed(
    onShowSnackbar: ((String) -> Unit)?,
    modifier: Modifier = Modifier,
    onEmpty: @Composable () -> Unit = {},
    onLoading: @Composable () -> Unit = { RainbowProgressIndicator(modifier) },
    onFailure: @Composable (Throwable) -> Unit = { if (onShowSnackbar != null) onShowSnackbar(it.message.toString()) },
    onSuccess: @Composable (T) -> Unit,
) {
    when (this) {
        is UIState.Empty -> onEmpty()
        is UIState.Loading -> onLoading()
        is UIState.Success -> onSuccess(value)
        is UIState.Failure -> onFailure(exception)
    }
}

fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
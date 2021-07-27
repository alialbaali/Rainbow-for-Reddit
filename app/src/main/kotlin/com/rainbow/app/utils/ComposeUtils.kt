package com.rainbow.app.utils

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rainbow.app.comment.RainbowProgressIndicator
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val RainbowIcons = Icons.Rounded

inline fun Modifier.onHoverChanged(enabled: Boolean = true, crossinline onValueChange: (Boolean) -> Unit): Modifier {
    return if (enabled)
        pointerMoveFilter(
            onExit = {
                onValueChange(false)
                false
            },
            onEnter = {
                onValueChange(true)
                false
            }
        )
    else
        this
}


fun Modifier.defaultPadding() = padding(16.dp)

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
    actionLabel: String = "Hide",
    onSuccess: @Composable (T) -> Unit,
) {

    when (this) {
        is UIState.Loading -> RainbowProgressIndicator()
        is UIState.Success -> onSuccess(value)
        is UIState.Failure -> {

            val snackbarHostState = remember { SnackbarHostState() }

            SnackbarHost(snackbarHostState, modifier = Modifier.defaultPadding())

            LaunchedEffect(Unit) {
                snackbarHostState.showSnackbar(exception.message.toString(), actionLabel)
            }

        }

    }

}

fun LocalDateTime.Companion.now() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun Modifier.handy() = border(5.dp, color = Color.Red)
package com.rainbow.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.Navigator
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.arkivanov.decompose.lifecycle.destroy
import com.arkivanov.decompose.lifecycle.resume
import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.ParcelableContainer
import com.arkivanov.decompose.statekeeper.StateKeeper
import com.arkivanov.decompose.statekeeper.StateKeeperDispatcher

private const val KeyState = "State"

@Composable
inline fun <reified T : Parcelable> Navigation(
    noinline initialScreen: () -> T,
    noinline initialBackStack: () -> List<T> = ::emptyList,
    noinline animation: @Composable (T, @Composable (T) -> Unit) -> Unit = { configuration, contentCallback ->
        contentCallback(
            configuration
        )
    },
    noinline content: @Composable Navigator<T>.(T) -> Unit,
) {

    val context = rememberComponentContext()

    val router = remember {
        context.router(
            initialConfiguration = initialScreen,
            configurationClass = T::class,
            initialBackStack = initialBackStack,
        ) { configuration, _ -> configuration }
    }

//    Children(
//        routerState = router.state,
//        animation = { _, configuration, contentCallback ->
//            animation(configuration) { contentCallback(it, it) }
//        }
//    ) { _, configuration -> router.content(configuration) }
}

@Composable
fun rememberComponentContext(): DefaultComponentContext {

    val lifecycleRegistry = rememberLifecycleRegistry()

    val stateKeeper = rememberStateKeeper()

    return remember { DefaultComponentContext(lifecycleRegistry, stateKeeper) }
}


@Composable
private fun rememberLifecycleRegistry(): LifecycleRegistry {
    val lifecycle = remember { LifecycleRegistry() }

    DisposableEffect(Unit) {
        lifecycle.resume()
        onDispose {
            lifecycle.destroy()
        }
    }
    return lifecycle
}

@Composable
private fun rememberStateKeeper(): StateKeeper {

    val savableStateRegistry = LocalSaveableStateRegistry.current

    val dispatcher = remember {
        StateKeeperDispatcher(savableStateRegistry?.consumeRestored(KeyState) as ParcelableContainer?)
    }

    savableStateRegistry?.let {
        DisposableEffect(Unit) {
            val entry = savableStateRegistry.registerProvider(KeyState, dispatcher::save)
            onDispose {
                entry.unregister()
            }
        }
    }

    return dispatcher
}
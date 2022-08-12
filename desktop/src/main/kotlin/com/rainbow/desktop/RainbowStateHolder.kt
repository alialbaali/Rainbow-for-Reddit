package com.rainbow.desktop

import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(FlowPreview::class)
object RainbowStateHolder : StateHolder() {

    private val mutableScreen = MutableStateFlow<Screen>(Screen.NavigationItem.Home)
    val screen get() = mutableScreen.asStateFlow()

    private val mutableBackStack = MutableStateFlow<List<Screen>>(emptyList())
    val backStack get() = mutableBackStack.asStateFlow()

    private val mutableForwardStack = MutableStateFlow<List<Screen>>(emptyList())
    val forwardStack get() = mutableBackStack.asStateFlow()

    private val mutableContentScreen = MutableStateFlow<ContentScreen>(ContentScreen.None)
    val contentScreen get() = mutableContentScreen.asStateFlow()

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    init {
//        mutableRefreshContent
//            .debounce(Constants.RefreshContentDebounceTime)
//            .onEach {
//                when (screen.value) {
//                    Screen.NavigationItem.Home -> loadHomePosts(postSorting.value, timeSorting.value)
//                    Screen.NavigationItem.Messages -> TODO()
//                    Screen.NavigationItem.Profile -> TODO()
//                    Screen.NavigationItem.Settings -> TODO()
//                    Screen.NavigationItem.Subreddits -> TODO()
//                    is Screen.Search -> TODO()
//                    is Screen.Subreddit -> TODO()
//                    is Screen.User -> TODO()
//                }
//            }
//            .launchIn(scope)
    }

    fun navigateBack() {
        mutableForwardStack.value = forwardStack.value + screen.value
        mutableScreen.value = backStack.value.last()
        mutableBackStack.value = backStack.value.dropLast(1)
    }

    fun navigateForward() {
        mutableBackStack.value = backStack.value + screen.value
        mutableScreen.value = forwardStack.value.last()
        mutableForwardStack.value = forwardStack.value.dropLast(1)
    }

    fun navigateToScreen(screen: Screen) {
        mutableBackStack.value = backStack.value + this.screen.value
        mutableScreen.value = screen
        mutableForwardStack.value = emptyList()
    }

    fun navigateToContentScreen(contentScreen: ContentScreen) {
        mutableContentScreen.value = contentScreen
    }

    fun refreshContent() {
        mutableRefreshContent.tryEmit(Unit)
    }
}
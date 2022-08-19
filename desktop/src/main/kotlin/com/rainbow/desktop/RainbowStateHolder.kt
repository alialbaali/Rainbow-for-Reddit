package com.rainbow.desktop

import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@OptIn(FlowPreview::class)
object RainbowStateHolder : StateHolder() {

    private val mutableMainScreen = MutableStateFlow<MainScreen>(MainScreen.SidebarItem.Home)
    val mainScreen get() = mutableMainScreen.asStateFlow()

    private val mutableBackStack = MutableStateFlow<List<MainScreen>>(emptyList())
    val backStack get() = mutableBackStack.asStateFlow()

    private val mutableForwardStack = MutableStateFlow<List<MainScreen>>(emptyList())
    val forwardStack get() = mutableBackStack.asStateFlow()

    private val mutableDetailsScreen = MutableStateFlow<DetailsScreen>(DetailsScreen.None)
    val detailsScreen get() = mutableDetailsScreen.asStateFlow()

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    init {
//        mutableRefreshContent
//            .debounce(Constants.RefreshContentDebounceTime)
//            .onEach {
//                when (mainScreen.value) {
//                    MainScreen.SidebarItem.Home -> loadHomePosts(postSorting.value, timeSorting.value)
//                    MainScreen.SidebarItem.Messages -> TODO()
//                    MainScreen.SidebarItem.Profile -> TODO()
//                    MainScreen.SidebarItem.Settings -> TODO()
//                    MainScreen.SidebarItem.Subreddits -> TODO()
//                    is MainScreen.Search -> TODO()
//                    is MainScreen.Subreddit -> TODO()
//                    is MainScreen.User -> TODO()
//                }
//            }
//            .launchIn(scope)
    }

    fun navigateBack() {
        mutableForwardStack.value = forwardStack.value + mainScreen.value
        mutableMainScreen.value = backStack.value.last()
        mutableBackStack.value = backStack.value.dropLast(1)
    }

    fun navigateForward() {
        mutableBackStack.value = backStack.value + mainScreen.value
        mutableMainScreen.value = forwardStack.value.last()
        mutableForwardStack.value = forwardStack.value.dropLast(1)
    }

    fun navigateToMainScreen(mainScreen: MainScreen) {
        mutableBackStack.value = backStack.value + this.mainScreen.value
        mutableMainScreen.value = mainScreen
        mutableForwardStack.value = emptyList()
    }

    fun navigateToDetailsScreen(detailsScreen: DetailsScreen) {
        mutableDetailsScreen.value = detailsScreen
    }

    fun refreshContent() {
        mutableRefreshContent.tryEmit(Unit)
    }
}
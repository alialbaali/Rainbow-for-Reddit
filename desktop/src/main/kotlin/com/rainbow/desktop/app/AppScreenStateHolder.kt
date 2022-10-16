package com.rainbow.desktop.app

import com.rainbow.desktop.all.AllScreenStateHolder
import com.rainbow.desktop.home.HomeScreenStateHolder
import com.rainbow.desktop.home.HomeTab
import com.rainbow.desktop.message.MessageTab
import com.rainbow.desktop.message.MessagesScreenStateHolder
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.popular.PopularScreenStateHolder
import com.rainbow.desktop.profile.ProfileScreenStateHolder
import com.rainbow.desktop.profile.ProfileTab
import com.rainbow.desktop.search.SearchScreenStateHolder
import com.rainbow.desktop.search.SearchTab
import com.rainbow.desktop.state.StateHolder
import com.rainbow.desktop.subreddit.SubredditScreenStateHolder
import com.rainbow.desktop.subreddit.SubredditTab
import com.rainbow.desktop.subreddit.SubredditsScreenStateHolder
import com.rainbow.desktop.user.UserScreenStateHolder
import com.rainbow.desktop.user.UserTab
import com.rainbow.desktop.utils.Constants
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

@OptIn(FlowPreview::class)
object AppScreenStateHolder : StateHolder() {

    private val mutableMainScreen = MutableStateFlow<MainScreen>(MainScreen.SidebarItem.Home)
    val mainScreen get() = mutableMainScreen.asStateFlow()
    val sidebarItem
        get() = mainScreen.map {
            it as? MainScreen.SidebarItem ?: backStack.value
                .last { it is MainScreen.SidebarItem } as MainScreen.SidebarItem
        }.stateIn(scope, SharingStarted.Eagerly, MainScreen.SidebarItem.Home)

    private val mutableBackStack = MutableStateFlow<List<MainScreen>>(emptyList())
    val backStack get() = mutableBackStack.asStateFlow()

    private val mutableForwardStack = MutableStateFlow<List<MainScreen>>(emptyList())
    val forwardStack get() = mutableForwardStack.asStateFlow()

    private val mutableDetailsScreen = MutableStateFlow<DetailsScreen>(DetailsScreen.None)
    val detailsScreen get() = mutableDetailsScreen.asStateFlow()

    private val mutableRefreshContent = MutableSharedFlow<Unit>(replay = 1)

    init {
        handleRefreshContent()
    }

    fun navigateBack() {
        mutableForwardStack.value += mainScreen.value
        mutableMainScreen.value = backStack.value.last()
        mutableBackStack.value = backStack.value.dropLast(1)
    }

    fun navigateForward() {
        mutableBackStack.value += mainScreen.value
        mutableMainScreen.value = forwardStack.value.last()
        mutableForwardStack.value = forwardStack.value.dropLast(1)
    }

    fun navigateToMainScreen(mainScreen: MainScreen) {
        mutableBackStack.value += this.mainScreen.value
        mutableMainScreen.value = mainScreen
        mutableForwardStack.value = emptyList()
    }

    fun navigateToDetailsScreen(detailsScreen: DetailsScreen) {
        mutableDetailsScreen.value = detailsScreen
    }

    fun refreshContent() {
        mutableRefreshContent.tryEmit(Unit)
    }

    private fun handleRefreshContent() {
        mutableRefreshContent
            .debounce(Constants.RefreshContentDebounceTime)
            .onEach {
                when (mainScreen.value) {
                    MainScreen.SidebarItem.Home -> {
                        val stateHolder = HomeScreenStateHolder.Instance
                        with(stateHolder) {
                            when (selectedTab.value) {
                                HomeTab.Posts -> postsStateHolder.loadItems()
                                HomeTab.Comments -> commentsStateHolder.loadItems()
                            }
                        }
                    }

                    MainScreen.SidebarItem.Messages -> {
                        val stateHolder = MessagesScreenStateHolder.Instance
                        with(stateHolder) {
                            when (selectedTab.value) {
                                MessageTab.Inbox -> inboxMessages.loadItems()
                                MessageTab.Unread -> unreadMessages.loadItems()
                                MessageTab.Sent -> sentMessages.loadItems()
                                MessageTab.Messages -> messages.loadItems()
                                MessageTab.Mentions -> mentions.loadItems()
                                MessageTab.PostMessages -> postMessages.loadItems()
                                MessageTab.CommentMessages -> commentMessages.loadItems()
                            }
                        }
                    }

                    MainScreen.SidebarItem.Profile -> {
                        val stateHolder = ProfileScreenStateHolder.Instance
                        with(stateHolder) {
                            when (selectedTab.value) {
                                ProfileTab.Overview -> overviewItemsStateHolder.loadItems()
                                ProfileTab.Submitted -> submittedPostsStateHolder.loadItems()
                                ProfileTab.Saved -> savedItemsStateHolder.loadItems()
                                ProfileTab.Hidden -> hiddenPostsStateHolder.loadItems()
                                ProfileTab.Upvoted -> upvotedPostsStateHolder.loadItems()
                                ProfileTab.Downvoted -> downvotedPostsStateHolder.loadItems()
                                ProfileTab.Comments -> commentsStateHolder.loadItems()
                                ProfileTab.Karma -> stateHolder.loadKarma()
                            }
                        }
                    }

                    MainScreen.SidebarItem.Settings -> {}
                    MainScreen.SidebarItem.Subreddits -> {
                        val stateHolder = SubredditsScreenStateHolder.Instance
                        stateHolder.subredditsStateHolder.loadItems()
                    }

                    is MainScreen.Search -> {
                        val stateHolder = SearchScreenStateHolder.CurrentInstance ?: return@onEach
                        with(stateHolder) {
                            when (selectedTab.value) {
                                SearchTab.Subreddits -> subredditsStateHolder.loadItems()
                                SearchTab.Posts -> postsStateHolder.loadItems()
                                SearchTab.Users -> usersStateHolder.loadItems()
                            }
                        }
                    }

                    is MainScreen.Subreddit -> {
                        val stateHolder = SubredditScreenStateHolder.CurrentInstance ?: return@onEach
                        with(stateHolder) {
                            when (selectedTab.value) {
                                SubredditTab.Posts -> postsStateHolder.loadItems()
                                SubredditTab.About -> {}
                                SubredditTab.Wiki -> loadWiki()
                                SubredditTab.Rules -> loadRules()
                                SubredditTab.Moderators -> loadModerators()
                            }
                        }
                    }

                    is MainScreen.User -> {
                        val stateHolder = UserScreenStateHolder.CurrentInstance ?: return@onEach
                        with(stateHolder) {
                            when (selectedTab.value) {
                                UserTab.Overview -> itemsStateHolder.loadItems()
                                UserTab.Submitted -> postsStateHolder.loadItems()
                                UserTab.Comments -> commentsStateHolder.loadItems()
                            }
                        }
                    }

                    MainScreen.SidebarItem.All -> {
                        val stateHolder = AllScreenStateHolder.Instance
                        stateHolder.postsStateHolder.loadItems()
                    }

                    MainScreen.SidebarItem.Popular -> {
                        val stateHolder = PopularScreenStateHolder.Instance
                        stateHolder.postsStateHolder.loadItems()
                    }
                }
            }
            .launchIn(scope)
    }
}
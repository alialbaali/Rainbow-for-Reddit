package com.rainbow.desktop.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.icons.rounded.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.components.*
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.posts
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.*
import com.rainbow.domain.models.*

@Composable
fun SubredditScreen(
    subredditName: String,
    onNavigateMainScreen: (MainScreen) -> Unit,
    onNavigateDetailsScreen: (DetailsScreen) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val stateHolder = remember { SubredditScreenStateHolder.getInstance(subredditName) }
    val postsState by stateHolder.postsStateHolder.items.collectAsState()
    val moderatorsState by stateHolder.moderators.collectAsState()
    val subredditState by stateHolder.subreddit.collectAsState()
    val selectedTab by stateHolder.selectedTab.collectAsState()
    val rulesState by stateHolder.rules.collectAsState()
    val wikiState by stateHolder.wiki.collectAsState()
    val postSorting by stateHolder.postsStateHolder.sorting.collectAsState()
    val timeSorting by stateHolder.postsStateHolder.timeSorting.collectAsState()
    val selectedItemId by stateHolder.selectedItemId.collectAsState()
    val flairsState by stateHolder.flairs.collectAsState()
    val isFlairsEnabled by stateHolder.isFlairsEnabled.collectAsState()

    RainbowLazyColumn(modifier) {
        subredditState.fold(
            onEmpty = {},
            onFailure = { value, exception -> },
            onLoading = {
                item {
                    RainbowProgressIndicator()
                }
            },
            onSuccess = { subreddit ->
                item {
                    Header(
                        subreddit,
                        isFlairsEnabled,
                        flairsState,
                        onFlairsEnabled = { isEnabled ->
                            if (isEnabled) stateHolder.enableFlairs() else stateHolder.disableFlairs()
                        },
                        stateHolder::selectFlair,
                        stateHolder::clearFlair,
                        stateHolder::loadFlairs,
                        onShowSnackbar,
                    )
                }

                item {
                    ScrollableEnumTabRow(
                        selectedTab = selectedTab,
                        onTabClick = { stateHolder.selectTab(it) },
                    )
                }

                if (selectedTab == SubredditTab.Posts) {
                    item {
                        PostSorting(
                            postSorting,
                            timeSorting,
                            stateHolder.postsStateHolder::setSorting,
                            stateHolder.postsStateHolder::setTimeSorting,
                        )
                    }
                }

                when (selectedTab) {
                    SubredditTab.Posts -> posts(
                        postsState,
                        onNavigateMainScreen,
                        onNavigateDetailsScreen = { detailsScreen ->
                            if (detailsScreen is DetailsScreen.Post) {
                                stateHolder.selectItemId(detailsScreen.postId)
                            }
                            onNavigateDetailsScreen(detailsScreen)
                        },
                        {},
                        onShowSnackbar,
                        stateHolder.postsStateHolder::setLastItem,
                    )

                    SubredditTab.About -> about(subreddit)
                    SubredditTab.Wiki -> wiki(wikiState, onShowSnackbar)
                    SubredditTab.Rules -> rules(rulesState, onShowSnackbar)
                    SubredditTab.Moderators -> moderators(
                        moderatorsState,
                        { onNavigateMainScreen(MainScreen.User(it)) },
                        onShowSnackbar
                    )
                }
            }
        )
    }

    DisposableEffect(selectedTab, selectedItemId) {
        if (selectedTab == SubredditTab.Posts) {
            selectedItemId?.let { postId ->
                onNavigateDetailsScreen(DetailsScreen.Post(postId))
            }
        } else {
//            onNavigateDetailsScreen(DetailsScreen.None)
        }
        onDispose {
//            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }
}

private fun LazyListScope.wiki(wikiState: UIState<WikiPage>, onShowSnackbar: (String) -> Unit) {
    item {
        wikiState.composed(onShowSnackbar) { wikiPage ->
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                    .defaultPadding()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(wikiPage.content)
                Text("Revisioned by ${wikiPage.revisionBy.name} on ${wikiPage.revisionDate}")
            }
        }
    }
}

private fun LazyListScope.about(subreddit: Subreddit) {
    item {
        MarkdownText(
            subreddit.longDescription,
            Modifier
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                .defaultPadding()
                .fillMaxWidth()
        )
    }
}

@Composable
private fun Header(
    subreddit: Subreddit,
    isFlairsEnabled: Boolean,
    flairsState: UIState<List<Pair<Flair, Boolean>>>,
    onFlairsEnabled: (Boolean) -> Unit,
    onFlairClick: (Flair) -> Unit,
    onClearFlairClick: () -> Unit,
    onLoadFlairs: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier
            .heightIn(min = ScreenHeaderContentMinHeight)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.height(IntrinsicSize.Max)) {
            ScreenHeader(
                subreddit.bannerImageUrl.toString(),
                subreddit.imageUrl.toString(),
                subreddit.name,
                imageShape = MaterialTheme.shapes.large,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .defaultPadding(start = 232.dp),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                ScreenHeaderDescription(subreddit.shortDescription)
                Spacer(Modifier.height(RainbowTheme.dpDimensions.medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FlairButton(
                        isFlairsEnabled,
                        flairsState,
                        onFlairsEnabled,
                        onFlairClick,
                        onClearFlairClick,
                        onLoadFlairs
                    )
                    Spacer(Modifier.width(RainbowTheme.dpDimensions.medium))
                    CopyLinkIconButton(subreddit.fullUrl, onShowSnackbar)
                    Spacer(Modifier.width(RainbowTheme.dpDimensions.medium))
                    SubredditFavoriteIconButton(
                        subreddit,
                        onShowSnackbar,
                        enabled = subreddit.isSubscribed
                    )
                    Spacer(Modifier.width(RainbowTheme.dpDimensions.medium))
                    SubscribeButton(subreddit, onShowSnackbar)
                }
            }
        }
    }
}

@Composable
private fun FlairButton(
    isFlairsEnabled: Boolean,
    flairsState: UIState<List<Pair<Flair, Boolean>>>,
    onFlairsEnabled: (Boolean) -> Unit,
    onFlairClick: (Flair) -> Unit,
    onNoneClick: () -> Unit,
    onLoadFlairs: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val flairs = remember(flairsState) { flairsState.getOrDefault(emptyList()) }
    val isNoneSelected = remember(flairs) { flairs.none { it.second } }
    RainbowDropdownMenuHolder(
        onClick = onLoadFlairs,
        text = { Text(RainbowStrings.Flair) },
        icon = { Icon(RainbowIcons.Label, RainbowStrings.Flair) },
        modifier = modifier,
    ) { handler ->
        if (!flairsState.isLoading) {
//            SettingsOption(RainbowStrings.Flairs, Modifier.padding(horizontal = RainbowTheme.dpDimensions.medium)) {
//                Switch(isFlairsEnabled, onFlairsEnabled)
//            }

            RainbowDropdownMenuItem(
                isNoneSelected,
                onClick = {
                    onNoneClick()
                    handler.hideMenu()
                },
            ) {
                Text(RainbowStrings.None, style = MaterialTheme.typography.labelLarge)
            }
        }

        flairs.forEach { flair ->
            RainbowDropdownMenuItem(
                flair.second,
                onClick = {
                    onFlairClick(flair.first)
                    handler.hideMenu()
                },
//                enabled = isFlairsEnabled,
            ) {
                FlairItem(flair.first, FlairStyle.Default)
            }
        }

        if (flairsState.isLoading) {
            RainbowProgressIndicator()
        }
    }
}

@Composable
private fun CopyLinkIconButton(
    link: String,
    onLinkCopied: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    RainbowIconButton(
        onClick = {
            val annotatedString = AnnotatedString(link)
            clipboardManager.setText(annotatedString)
            onLinkCopied(RainbowStrings.LinkIsCopied)
        },
        modifier = modifier
    ) {
        Icon(RainbowIcons.Link, RainbowStrings.Link)
    }
}

@Composable
private fun SelectableFlairItem(
    flair: Pair<Flair, Boolean>,
    onClick: (Flair) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick(flair.first) }
            .padding(horizontal = RainbowTheme.dpDimensions.medium)
            .background(MaterialTheme.colorScheme.surface)
            .padding(RainbowTheme.dpDimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Spacer(Modifier.width(RainbowTheme.dpDimensions.medium))
        if (flair.second) {
            Icon(RainbowIcons.Done, RainbowStrings.Select)
        }
    }
}

@Composable
private fun FlairsActions(onApply: () -> Unit, onClear: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .defaultPadding(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RainbowButton(onClear) {
            Text(RainbowStrings.Clear)
        }

        Spacer(Modifier.width(RainbowTheme.dpDimensions.medium))

        RainbowButton(
            onApply,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Text(RainbowStrings.Apply)
        }
    }
}


private fun LazyListScope.moderators(
    moderatorsState: UIState<List<Moderator>>,
    onModeratorClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
) {
    item {
        moderatorsState.composed(onShowSnackbar) { moderators ->
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                    .defaultPadding()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                moderators.forEach {
                    ModeratorItem(it, onModeratorClick, Modifier.fillMaxWidth())
                }
            }
        }
    }
}

private fun LazyListScope.rules(rulesState: UIState<List<Rule>>, onShowSnackbar: (String) -> Unit) {
    item {
        rulesState.composed(onShowSnackbar) { rules ->
            Column(
                Modifier
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                    .defaultPadding()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rules.forEach {
                    RuleItem(it, Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun RuleItem(rule: Rule, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("${rule.priority}. ${rule.title}", fontWeight = FontWeight.Medium, fontSize = 18.sp)
        Text(rule.description)
    }
}

@Composable
private fun ModeratorItem(moderator: Moderator, onModeratorClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .clip(MaterialTheme.shapes.large)
            .clickable { onModeratorClick(moderator.name) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(moderator.name, fontWeight = FontWeight.Medium, fontSize = 18.sp)
            moderator.permissions.forEach { permission ->
                Text(
                    text = permission.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                        .padding(8.dp)
                )
            }
        }
//        Text(moderator.modSince.toJavaLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME))
    }
}
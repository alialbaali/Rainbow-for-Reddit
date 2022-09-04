package com.rainbow.desktop.subreddit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.components.*
import com.rainbow.desktop.navigation.DetailsScreen
import com.rainbow.desktop.navigation.MainScreen
import com.rainbow.desktop.post.posts
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

    DisposableEffect(postsState.getOrDefault(emptyList()).isEmpty()) {
        val post = postsState.getOrNull()?.firstOrNull()
        if (post != null) {
            onNavigateDetailsScreen(DetailsScreen.Post(post.id))
        }
        onDispose {
            onNavigateDetailsScreen(DetailsScreen.None)
        }
    }

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
                    Header(subreddit, onShowSnackbar, Modifier.padding(bottom = 8.dp))
                }
                item {
                    ScrollableEnumTabRow(
                        selectedTab = selectedTab,
                        onTabClick = { stateHolder.selectTab(it) },
                    )
                }
            }
        )
        when (selectedTab) {
            SubredditTab.Posts -> posts(
                postsState,
                onNavigateMainScreen,
                onNavigateDetailsScreen,
                {},
                onShowSnackbar,
                stateHolder.postsStateHolder::setLastItem,
            )

            SubredditTab.Description -> description(subredditState, onShowSnackbar)
            SubredditTab.Wiki -> wiki(wikiState, onShowSnackbar)
            SubredditTab.Rules -> rules(rulesState, onShowSnackbar)
            SubredditTab.Moderators -> moderators(
                moderatorsState,
                { onNavigateMainScreen(MainScreen.User(it)) },
                onShowSnackbar
            )
        }
    }
}

private fun LazyListScope.wiki(wikiState: UIState<WikiPage>, onShowSnackbar: (String) -> Unit) {
    item {
        wikiState.composed(onShowSnackbar) { wikiPage ->
            Column(
                Modifier
                    .defaultSurfaceShape()
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

private fun LazyListScope.description(state: UIState<Subreddit>, onShowSnackbar: (String) -> Unit) {
    item {
        state.composed(onShowSnackbar) {
            MarkdownText(
                it.longDescription,
                Modifier.defaultSurfaceShape()
                    .defaultPadding()
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Header(
    subreddit: Subreddit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier
            .heightIn(min = 350.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            ScreenHeaderItem(
                subreddit.bannerImageUrl.toString(),
                subreddit.imageUrl.toString(),
                subreddit.name,
                imageShape = MaterialTheme.shapes.large,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .defaultPadding(start = 232.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = subreddit.shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis,
                )
                Row {
                    SubredditFavoriteIconButton(
                        subreddit,
                        onShowSnackbar,
                        enabled = subreddit.isSubscribed
                    )
                    SelectFlairButton(subreddit.name, onShowSnackbar)
                    SubscribeButton(subreddit, onShowSnackbar)
                }
            }
        }
    }
}

@Composable
private fun SelectFlairButton(subredditName: String, onShowSnackbar: (String) -> Unit, modifier: Modifier = Modifier) {
    var isDialogVisible by remember { mutableStateOf(false) }
    OutlinedButton(onClick = { isDialogVisible = true }, modifier) {
        Text(RainbowStrings.Flair)
    }
    AnimatedVisibility(isDialogVisible) {
        SelectFlairDialog(subredditName, onCloseRequest = { isDialogVisible = false }, onShowSnackbar)
    }
}

@Composable
private fun SelectFlairDialog(
    subredditName: String,
    onCloseRequest: () -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
//    Dialog(onCloseRequest) {
//        state.composed(onShowSnackbar, modifier) { flairs ->
//            val scrollState = rememberLazyListState()
//            var boxPadding by remember { mutableStateOf(0) }
//            Box(modifier.fillMaxSize()) {
//                LazyColumn(
//                    Modifier.padding(bottom = boxPadding.dp),
//                    state = scrollState,
//                    verticalArrangement = Arrangement.spacedBy(16.dp),
//                    contentPadding = PaddingValues(16.dp)
//                ) {
//                    items(flairs) { flair ->
//                        SelectFlairItem(
//                            flair,
//                            onClick = { state = state.map { it.map { it.first to (it.first == flair.first) } } })
//                    }
//                }
//
////                VerticalScrollbar(
////                    rememberScrollbarAdapter(scrollState),
////                    modifier = Modifier
////                        .fillMaxHeight()
////                        .align(Alignment.CenterEnd)
////                )
//
//                SelectFlairActions(
//                    onApply = {
//                        flairs.firstOrNull { it.second }?.let {
//                            scope.launch {
//                                Repos.Subreddit.selectFlair(subredditName, it.first.id)
//                            }
//                        }
//                        onCloseRequest()
//                    },
//                    onClear = {
//                        state = state.map { it.map { it.first to false } }
//                        scope.launch {
//                            Repos.Subreddit.unselectFlair(subredditName)
//                        }
//                        onCloseRequest()
//                    },
//                    Modifier
//                        .align(Alignment.BottomEnd)
//                        .onSizeChanged { boxPadding = it.height }
//                )
//            }
//        }

//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectFlairItem(flair: Pair<Flair, Boolean>, onClick: (Flair) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick(flair.first) },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(flair.second, onClick = null)
        FlairItem(flair.first)
    }
}

@Composable
private fun SelectFlairActions(onApply: () -> Unit, onClear: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier
            .background(MaterialTheme.colorScheme.background)
            .wrapContentSize(Alignment.BottomEnd),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(onClick = onClear) {
            Text(RainbowStrings.Clear)
        }
        Button(onApply) {
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
                    .defaultSurfaceShape()
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
                    .defaultSurfaceShape()
                    .fillMaxWidth()
                    .defaultPadding(),
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
                        .defaultSurfaceShape()
                        .padding(8.dp)
                )
            }
        }
//        Text(moderator.modSince.toJavaLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME))
    }
}
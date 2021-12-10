package com.rainbow.app.subreddit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.components.FlairItem
import com.rainbow.app.components.HeaderItem
import com.rainbow.app.components.Markdown
import com.rainbow.app.model.ListModel
import com.rainbow.app.post.posts
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.*
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

enum class SubredditTab {
    Posts, Description, Wiki, Rules, Moderators;

    companion object {
        val Default = Posts
    }
}

@Composable
fun SubredditScreen(
    subredditName: String,
    focusRequester: FocusRequester,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    setPostModel: (ListModel<*>) -> Unit,
    onPostUpdate: (Post) -> Unit,
    onPostClick: (Post) -> Unit,
    modifier: Modifier = Modifier,
) {
    val model = remember { SubredditScreenModel.getOrCreateInstance(subredditName) }
    setPostModel(model.postListModel)
    val scrollingState = rememberLazyListState()
    val postLayout by model.postListModel.postLayout.collectAsState()
    val postsState by model.postListModel.items.collectAsState()
    val moderatorsState by model.moderators.collectAsState()
    val subredditState by model.subreddit.collectAsState()
    val selectedTab by model.selectedTab.collectAsState()
    val rulesState by model.rules.collectAsState()
    val wikiState by model.wiki.collectAsState()
    LazyColumn(modifier, scrollingState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            subredditState.composed(onShowSnackbar) { subreddit ->
                Header(subreddit, onShowSnackbar, Modifier.padding(bottom = 8.dp))
            }
        }
        item {
            DefaultTabRow(
                selectedTab = selectedTab,
                onTabClick = { model.selectTab(it) },
            )
        }
        when (selectedTab) {
            SubredditTab.Posts -> posts(
                postsState,
                onPostUpdate,
                postLayout,
                focusRequester,
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                model.postListModel::setLastItem,
                onPostClick,
            )
            SubredditTab.Description -> description(subredditState, onShowSnackbar)
            SubredditTab.Wiki -> {
                if (model.wiki.value.isLoading)
                    model.loadWiki()
                wiki(wikiState, onShowSnackbar)
            }
            SubredditTab.Rules -> {
                if (model.rules.value.isLoading)
                    model.loadRules()
                rules(rulesState, onShowSnackbar)
            }
            SubredditTab.Moderators -> {
                if (model.moderators.value.isLoading)
                    model.loadModerators()
                moderators(moderatorsState, onUserNameClick, onShowSnackbar)
            }
        }
    }
    VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
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
            Markdown(
                it.longDescription,
                Modifier.defaultSurfaceShape()
                    .defaultPadding()
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Header(subreddit: Subreddit, onShowSnackbar: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .defaultSurfaceShape()
            .heightIn(min = 350.dp)
            .fillMaxWidth()
    ) {
        HeaderItem(
            subreddit.bannerImageUrl.toString(),
            subreddit.imageUrl.toString(),
            subreddit.name,
            imageShape = MaterialTheme.shapes.large,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .defaultPadding(start = 232.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = subreddit.shortDescription,
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1F)
            )
            SubredditFavoriteIconButton(subreddit, onShowSnackbar, enabled = subreddit.isSubscribed)
            SelectFlairButton(subreddit.name, onShowSnackbar)
            SubscribeButton(subreddit, onShowSnackbar)
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
    val scope = rememberCoroutineScope()
    var state by remember { mutableStateOf<UIState<List<Pair<Flair, Boolean>>>>(UIState.Loading) }
    LaunchedEffect(subredditName) {
        val currentSelectedFlair = Repos.Subreddit.getCurrentSubredditFlair(subredditName)
            .toUIState()
            .getOrNull()
        Repos.Subreddit.getSubredditFlairs(subredditName)
            .map { it.associateWith { it.id == currentSelectedFlair?.id }.toList() }
            .toUIState()
            .also { state = it }
    }


    Dialog(onCloseRequest) {
        state.composed(onShowSnackbar, modifier) { flairs ->
            val scrollState = rememberLazyListState()
            var boxPadding by remember { mutableStateOf(0) }
            Box(modifier.fillMaxSize()) {
                LazyColumn(
                    Modifier.padding(bottom = boxPadding.dp),
                    state = scrollState,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(flairs) { flair ->
                        SelectFlairItem(
                            flair,
                            onClick = { state = state.map { it.map { it.first to (it.first == flair.first) } } })
                    }
                }

                VerticalScrollbar(
                    rememberScrollbarAdapter(scrollState),
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.CenterEnd)
                )

                SelectFlairActions(
                    onApply = {
                        flairs.firstOrNull { it.second }?.let {
                            scope.launch {
                                Repos.Subreddit.selectFlair(subredditName, it.first.id)
                            }
                        }
                        onCloseRequest()
                    },
                    onClear = {
                        state = state.map { it.map { it.first to false } }
                        scope.launch {
                            Repos.Subreddit.unselectFlair(subredditName)
                        }
                        onCloseRequest()
                    },
                    Modifier
                        .align(Alignment.BottomEnd)
                        .onSizeChanged { boxPadding = it.height }
                )
            }
        }

    }
}

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
            .background(MaterialTheme.colors.background)
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
        Text(moderator.modSince.toJavaLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME))
    }
}
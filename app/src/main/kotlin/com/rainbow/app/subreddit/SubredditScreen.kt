package com.rainbow.app.subreddit

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.components.DefaultTabRow
import com.rainbow.app.components.HeaderItem
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.post.Sorting
import com.rainbow.app.post.posts
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter


enum class SubredditTab {
    Posts, Wiki, Rules, Moderators;

    companion object {
        val Default = Posts
    }
}

@Composable
fun SubredditScreen(
    subredditName: String,
    focusRequester: FocusRequester,
    onPostClick: (Post) -> Unit,
    onUserNameClick: (String) -> Unit,
    onSubredditNameClick: (String) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollingState = rememberLazyListState()
    var selectedTab by remember { mutableStateOf(SubredditTab.Default) }
    var postSorting by remember { mutableStateOf(SubredditPostSorting.Default) }
    var timeSorting by remember { mutableStateOf(TimeSorting.Default) }
    var lastPost by remember(postSorting, timeSorting) { mutableStateOf<Post?>(null) }
    val postLayout by Repos.Settings.postLayout.collectAsState(PostLayout.Card)
    val state by produceState<UIState<Subreddit>>(UIState.Loading, subredditName) {
        Repos.Subreddit.getSubreddit(subredditName)
            .map { it.toUIState() }
            .onEach { value = it }
            .launchIn(this)
    }
    val postsState by produceState<UIState<List<Post>>>(
        UIState.Loading,
        subredditName,
        postSorting,
        timeSorting,
        lastPost
    ) {
        if (lastPost == null)
            value = UIState.Loading
        Repos.Post.getSubredditPosts(subredditName, postSorting, timeSorting, lastPost?.id)
            .map { it.map { it.filterNot { it.isHidden } } }
            .map { it.toUIState() }
            .onEach { value = it }
            .launchIn(this)
    }
    val rulesState by produceState<UIState<List<Rule>>>(UIState.Loading, subredditName) {
        Repos.Rule.getSubredditRules(subredditName)
            .toUIState()
            .also { value = it }
    }
    val moderatorsState by produceState<UIState<List<Moderator>>>(UIState.Loading, subredditName) {
        Repos.Subreddit.getSubredditModerators(subredditName)
            .map { it.toUIState() }
            .onEach { value = it }
            .launchIn(this)
    }
    val wikiState by produceState<UIState<WikiPage>>(UIState.Loading, subredditName) {
        Repos.Subreddit.getWikiIndex(subredditName)
            .toUIState()
            .also { value = it }
    }
    LazyColumn(modifier, scrollingState, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
            state.composed { subreddit ->
                Header(subreddit, onShowSnackbar, Modifier.padding(bottom = 8.dp))
            }
        }
        item {
            DefaultTabRow(
                selectedTab = selectedTab,
                onTabClick = { selectedTab = it },
            )
        }
        item {
            if (selectedTab == SubredditTab.Posts)
                Sorting(
                    postSorting,
                    onSortingUpdate = { postSorting = it },
                    timeSorting,
                    onTimeSortingUpdate = { timeSorting = it },
                    Modifier.padding(top = 8.dp, bottom = 16.dp)
                )
        }
        when (selectedTab) {
            SubredditTab.Posts -> posts(
                postsState,
                postLayout,
                focusRequester,
                onPostClick,
                onUserNameClick,
                onSubredditNameClick,
                onShowSnackbar,
                onLoadMore = { lastPost = it }
            )
            SubredditTab.Wiki -> wiki(wikiState)
            SubredditTab.Rules -> rules(rulesState)
            SubredditTab.Moderators -> moderators(moderatorsState, onUserNameClick)
        }
    }
    VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
}

private fun LazyListScope.wiki(wikiState: UIState<WikiPage>) {
    when (wikiState) {
        is UIState.Empty -> item { Text("Empty") }
        is UIState.Failure -> item { Text("Failed to load") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> item {
            Column(
                Modifier
                    .defaultShape()
                    .defaultPadding()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(wikiState.value.content)
                Text("Revisioned by ${wikiState.value.revisionBy.name} on ${wikiState.value.revisionDate}")
            }
        }
    }
}

@Composable
private fun Header(subreddit: Subreddit, onShowSnackbar: (String) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier
            .defaultShape()
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
                text = subreddit.description,
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Ellipsis,
            )

            SubscribeButton(subreddit, onShowSnackbar)
        }
    }
}


private fun LazyListScope.moderators(moderatorsState: UIState<List<Moderator>>, onModeratorClick: (String) -> Unit) {
    when (moderatorsState) {
        is UIState.Empty -> item { Text("No moderators found.") }
        is UIState.Failure -> throw moderatorsState.exception
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            item {
                Column(
                    Modifier
                        .defaultShape()
                        .defaultPadding()
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    moderatorsState.value.forEach {
                        ModeratorItem(it, onModeratorClick, Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}

private fun LazyListScope.rules(rulesState: UIState<List<Rule>>) {
    when (rulesState) {
        is UIState.Empty -> item { Text("No rules found.") }
        is UIState.Failure -> item { Text("Failed to load rules.") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> {
            item {
                Column(
                    Modifier
                        .defaultShape()
                        .fillMaxWidth()
                        .defaultPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rulesState.value.forEach {
                        RuleItem(it, Modifier.fillMaxWidth())
                    }
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
                        .defaultShape()
                        .padding(8.dp)
                )
            }
        }
        Text(moderator.modSince.toJavaLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME))
    }
}
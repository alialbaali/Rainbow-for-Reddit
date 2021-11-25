package com.rainbow.app.subreddit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.PostAdd
import androidx.compose.material.icons.rounded.RemoveCircle
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.MenuIconButton
import com.rainbow.app.components.RainbowMenu
import com.rainbow.app.components.RainbowMenuItem
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultShape
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.fullUrl
import kotlinx.coroutines.launch

@Composable
fun SubredditItem(
    subreddit: Subreddit,
    onClick: (Subreddit) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier.defaultShape()
            .clickable { onClick(subreddit) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        SubredditItemHeader(subreddit)
        SubredditItemName(subreddit.name, Modifier.padding(horizontal = 16.dp))
        SubredditItemActions(subreddit, onShowSnackbar, Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp))
    }
}

@Composable
private fun SubredditItemActions(
    subreddit: Subreddit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val scope = rememberCoroutineScope()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SubredditFavoriteIconButton(subreddit, onShowSnackbar)
        Column {
            MenuIconButton(onClick = { isMenuExpanded = true })
            RainbowMenu(isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                RainbowMenuItem(
                    RainbowStrings.OpenInBrowser,
                    RainbowIcons.OpenInBrowser,
                    onclick = {
                        uriHandler.openUri(subreddit.fullUrl)
                        isMenuExpanded = false
                    }
                )
                RainbowMenuItem(
                    RainbowStrings.UnSubscribe,
                    RainbowIcons.RemoveCircle, // Playlist remove icon (Not available currently)
                    onclick = {
                        scope.launch {
                            Repos.Subreddit.unSubscribeSubreddit(subreddit.name)
                            isMenuExpanded = false
                            onShowSnackbar(RainbowStrings.UnsubscribeMessage(subreddit.name))
                        }
                    }
                )
                RainbowMenuItem(
                    RainbowStrings.CreatePost,
                    RainbowIcons.PostAdd,
                    onclick = {
                        onShowSnackbar(RainbowStrings.Todo)
                        isMenuExpanded = false
                    },
                )
            }
        }
    }
}
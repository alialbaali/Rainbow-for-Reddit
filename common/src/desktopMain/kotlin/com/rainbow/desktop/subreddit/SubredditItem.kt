package com.rainbow.desktop.subreddit

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
import com.rainbow.common.components.HeaderItem
import com.rainbow.common.components.MenuIconButton
import com.rainbow.common.subreddit.SubredditActionsModel
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings
import com.rainbow.common.utils.defaultSurfaceShape
import com.rainbow.desktop.components.RainbowMenu
import com.rainbow.desktop.components.RainbowMenuItem
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.fullUrl

@Composable
fun SubredditItem(
    subreddit: Subreddit,
    onSubredditUpdate: (Subreddit) -> Unit,
    onClick: (Subreddit) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.defaultSurfaceShape()
            .clickable { onClick(subreddit) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HeaderItem(subreddit.bannerImageUrl.toString(), subreddit.imageUrl.toString(), subreddit.name)
        SubredditItemName(subreddit.name, Modifier.padding(horizontal = 16.dp))
        SubredditItemActions(
            subreddit,
            onSubredditUpdate,
            onShowSnackbar,
            Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        )
    }
}

@Composable
private fun SubredditItemActions(
    subreddit: Subreddit,
    onSubredditUpdate: (Subreddit) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SubredditFavoriteIconButton(subreddit, onSubredditUpdate, onShowSnackbar)
        Column {
            MenuIconButton(onClick = { isMenuExpanded = true })
            RainbowMenu(isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                RainbowMenuItem(
                    RainbowStrings.OpenInBrowser,
                    RainbowIcons.OpenInBrowser,
                    onClick = {
                        uriHandler.openUri(subreddit.fullUrl)
                        isMenuExpanded = false
                    }
                )
                RainbowMenuItem(
                    RainbowStrings.UnSubscribe,
                    RainbowIcons.RemoveCircle, // Playlist remove icon (Not available currently)
                    onClick = {
                        SubredditActionsModel.unSubscribeSubreddit(subreddit, onSubredditUpdate)
                        isMenuExpanded = false
                        onShowSnackbar(RainbowStrings.UnsubscribeMessage(subreddit.name))
                    }
                )
                RainbowMenuItem(
                    RainbowStrings.CreatePost,
                    RainbowIcons.PostAdd,
                    onClick = {
                        onShowSnackbar(RainbowStrings.Todo)
                        isMenuExpanded = false
                    },
                )
            }
        }
    }
}
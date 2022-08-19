package com.rainbow.desktop.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.OpenInBrowser
import androidx.compose.material.icons.rounded.PlaylistRemove
import androidx.compose.material.icons.rounded.PostAdd
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.HeaderItem
import com.rainbow.desktop.components.MenuIconButton
import com.rainbow.desktop.components.RainbowMenu
import com.rainbow.desktop.components.RainbowMenuItem
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.Subreddit

@Composable
fun SubredditItem(
    subreddit: Subreddit,
    onClick: (Subreddit) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .clip(MaterialTheme.shapes.small)
            .clickable { onClick(subreddit) }
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.small),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HeaderItem(subreddit.bannerImageUrl.toString(), subreddit.imageUrl.toString(), subreddit.name)
        SubredditItemName(subreddit.name, Modifier.padding(horizontal = 16.dp))
        SubredditItemActions(
            subreddit,
            onShowSnackbar,
            Modifier.defaultPadding()
        )
    }
}

@Composable
private fun SubredditItemActions(
    subreddit: Subreddit,
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
        SubredditFavoriteIconButton(subreddit, onShowSnackbar)
        Column {
            MenuIconButton(onClick = { isMenuExpanded = true })
            RainbowMenu(isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                RainbowMenuItem(
                    RainbowStrings.OpenInBrowser,
                    RainbowIcons.OpenInBrowser,
                    onClick = {
                        uriHandler.openUri("subreddit.fullUrl")
                        isMenuExpanded = false
                    }
                )
                RainbowMenuItem(
                    RainbowStrings.UnSubscribe,
                    RainbowIcons.PlaylistRemove,
                    onClick = {
                        SubredditActionsStateHolder.unSubscribeSubreddit(subreddit)
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
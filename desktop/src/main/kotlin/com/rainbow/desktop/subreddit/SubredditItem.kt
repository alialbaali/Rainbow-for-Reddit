package com.rainbow.desktop.subreddit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PlaylistRemove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.*
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.fullUrl

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
        ItemHeader(subreddit.bannerImageUrl.toString(), subreddit.imageUrl.toString(), subreddit.name)
        SubredditItemName(subreddit.name, Modifier.padding(horizontal = 16.dp))
        SubredditItemOptions(
            subreddit,
            onShowSnackbar,
            Modifier
                .defaultPadding()
                .align(Alignment.End)
        )
    }
}

@Composable
private fun SubredditItemOptions(
    subreddit: Subreddit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OptionsSurface(modifier) {
        SubredditFavoriteIconButton(subreddit, onShowSnackbar)
        RainbowDropdownMenuHolder(
            icon = { Icon(RainbowIcons.MoreVert, RainbowStrings.SubredditOptions) },
        ) { handler ->
            OpenInBrowserDropdownMenuItem(subreddit.fullUrl, handler)
            CopyLinkDropdownMenuItem(subreddit.fullUrl, handler, onShowSnackbar)
            UnsubscribeDropdownMenuItem(subreddit, handler, onShowSnackbar)
        }
    }
}

@Composable
private fun UnsubscribeDropdownMenuItem(
    subreddit: Subreddit,
    handler: DropdownMenuHandler,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    RainbowDropdownMenuItem(
        onClick = {
            SubredditActionsStateHolder.unSubscribeSubreddit(subreddit)
            handler.hideMenu()
            onShowSnackbar(RainbowStrings.UnsubscribeMessage(subreddit.name))
        },
        modifier
    ) {
        Icon(RainbowIcons.PlaylistRemove, RainbowStrings.UnSubscribe)
        Text(RainbowStrings.UnSubscribe)
    }
}
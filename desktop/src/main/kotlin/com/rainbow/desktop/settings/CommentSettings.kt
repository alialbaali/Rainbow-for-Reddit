package com.rainbow.desktop.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.ItemSorting
import com.rainbow.desktop.utils.RainbowStrings

@Composable
fun CommentSettings(modifier: Modifier = Modifier) {
    SettingsTabContent(modifier) {
        CommentSortingOption()
        IsCommentsCollapsedOption()
    }
}

@Composable
private fun CommentSortingOption(modifier: Modifier = Modifier) {
    val commentSorting by SettingsStateHolder.postCommentSorting.collectAsState()
    SettingsOption(RainbowStrings.DefaultCommentSorting, modifier) {
        ItemSorting(
            commentSorting,
            SettingsStateHolder::setPostCommentSorting,
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
private fun IsCommentsCollapsedOption(modifier: Modifier = Modifier) {
    val isCommentsCollapsed by SettingsStateHolder.isCommentsCollapsed.collectAsState()
    SettingsOption(RainbowStrings.CollapseCommentsByDefault, modifier) {
        Switch(
            isCommentsCollapsed,
            onCheckedChange = { SettingsStateHolder.setIsCommentsCollapsed(it) },
        )
    }
}
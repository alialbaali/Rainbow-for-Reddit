package com.rainbow.common.settings

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.common.components.DropdownMenuHolder
import com.rainbow.common.utils.RainbowStrings

@Composable
fun CommentSettings(modifier: Modifier = Modifier) {
    SettingsTabContent(modifier) {
        CommentSortingOption()
        IsCommentsCollapsedOption()
    }
}

@Composable
private fun CommentSortingOption(modifier: Modifier = Modifier) {
    val commentSorting by SettingsModel.postCommentSorting.collectAsState()
    SettingsOption(RainbowStrings.DefaultCommentSorting, modifier) {
        DropdownMenuHolder(commentSorting, SettingsModel::setPostCommentSorting)
    }
}

@Composable
private fun IsCommentsCollapsedOption(modifier: Modifier = Modifier) {
    val isCommentsCollapsed by SettingsModel.isCommentsCollapsed.collectAsState()
    SettingsOption(RainbowStrings.CollapseCommentsByDefault, modifier) {
        Switch(
            isCommentsCollapsed,
            onCheckedChange = { SettingsModel.setIsCommentsCollapsed(it) },
        )
    }
}
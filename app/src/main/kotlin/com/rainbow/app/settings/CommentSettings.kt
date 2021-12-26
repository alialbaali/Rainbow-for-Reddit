package com.rainbow.app.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.rainbow.app.components.DropdownMenuHolder
import com.rainbow.app.utils.RainbowStrings

@Composable
fun CommentSettings(modifier: Modifier = Modifier) {
    val commentSorting by SettingsModel.postCommentSorting.collectAsState()
    SettingsTabContent(modifier) {
        SettingsOption(RainbowStrings.DefaultCommentSorting) {
            DropdownMenuHolder(commentSorting, SettingsModel::setPostCommentSorting)
        }
    }
}
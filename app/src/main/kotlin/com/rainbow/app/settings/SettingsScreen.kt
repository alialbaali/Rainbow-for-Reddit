package com.rainbow.app.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.EnumTabRow

enum class SettingsTab {
    General, Post, Comment;

    companion object {
        val Default = General
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val selectedTab by SettingsModel.selectedTab.collectAsState()
    Column(modifier.fillMaxWidth(0.5F), Arrangement.spacedBy(32.dp)) {
        EnumTabRow(selectedTab, SettingsModel::selectTab)
        AnimatedContent(selectedTab) { selectedTab ->
            when (selectedTab) {
                SettingsTab.General -> GeneralSettings()
                SettingsTab.Post -> PostSettings()
                SettingsTab.Comment -> CommentSettings()
            }
        }
    }
}
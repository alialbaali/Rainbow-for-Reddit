package com.rainbow.desktop.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.EnumTabRow
import com.rainbow.desktop.settings.SettingsModel
import com.rainbow.desktop.settings.SettingsTab

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val selectedTab by SettingsModel.selectedTab.collectAsState()
    Column(modifier, Arrangement.spacedBy(32.dp)) {
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
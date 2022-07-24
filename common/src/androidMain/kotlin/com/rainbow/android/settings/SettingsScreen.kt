package com.rainbow.android.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.android.navigation.Screen
import com.rainbow.common.components.ScrollableEnumTabRow
import com.rainbow.common.settings.SettingsModel
import com.rainbow.common.settings.SettingsTab

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsScreen(onNavigate: (Screen.Sheet) -> Unit, modifier: Modifier = Modifier) {
    val selectedTab by SettingsModel.selectedTab.collectAsState()
    Column(modifier, Arrangement.spacedBy(32.dp)) {
        ScrollableEnumTabRow(selectedTab, SettingsModel::selectTab)
        AnimatedContent(selectedTab) { selectedTab ->
            when (selectedTab) {
                SettingsTab.General -> GeneralSettings(onNavigate)
                SettingsTab.Post -> PostSettings(onNavigate)
                SettingsTab.Comment -> TODO()
            }
        }
    }
}
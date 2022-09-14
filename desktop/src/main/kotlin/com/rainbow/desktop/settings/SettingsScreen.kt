package com.rainbow.desktop.settings

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.DefaultContentPadding
import com.rainbow.desktop.utils.RainbowIcons

private const val SettingsTabFraction = 0.5F

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val selectedTab by SettingsStateHolder.selectedTab.collectAsState()
    Box(modifier.padding(DefaultContentPadding())) {
        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium)
        ) {
            SettingsTabs(selectedTab, SettingsStateHolder::selectTab)
            Crossfade(selectedTab) { animatedSelectedTab ->
                when (animatedSelectedTab) {
                    SettingsTab.General -> GeneralSettings(Modifier.fillMaxWidth(SettingsTabFraction))
                    SettingsTab.Post -> PostSettings(Modifier.fillMaxWidth(SettingsTabFraction))
                    SettingsTab.Comment -> CommentSettings(Modifier.fillMaxWidth(SettingsTabFraction))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsTabs(
    selectedTab: SettingsTab,
    onTabSelect: (SettingsTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier.width(IntrinsicSize.Max),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(RainbowTheme.dpDimensions.medium),
            verticalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium),
        ) {
            SettingsTab.values().forEach { tab ->
                val containerColor by animateColorAsState(
                    if (tab == selectedTab)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surface
                )
                val contentColor by animateColorAsState(
                    if (tab == selectedTab)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    selected = tab == selectedTab,
                    onClick = { onTabSelect(tab) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.small,
                    color = containerColor,
                    contentColor = contentColor,
                ) {
                    SettingsTabItem(tab)
                }
            }
        }
    }
}

@Composable
private fun SettingsTabItem(tab: SettingsTab, modifier: Modifier = Modifier) {
    Row(
        modifier.padding(RainbowTheme.dpDimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium),
    ) {
        Icon(tab.icon, tab.name)
        Text(tab.name, style = MaterialTheme.typography.titleMedium)
    }
}

private val SettingsTab.icon
    get() = when (this) {
        SettingsTab.General -> RainbowIcons.Settings
        SettingsTab.Post -> RainbowIcons.Article
        SettingsTab.Comment -> RainbowIcons.Forum
    }
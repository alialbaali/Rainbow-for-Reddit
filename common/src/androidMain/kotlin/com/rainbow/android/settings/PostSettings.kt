package com.rainbow.android.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.android.navigation.Screen
import com.rainbow.common.utils.RainbowStrings

@Composable
fun PostSettings(onNavigate: (Screen.Sheet) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier) {
        SettingsOption(
            RainbowStrings.DefaultHomePostSorting,
            onClick = { onNavigate(Screen.Sheet.HomePostSorting) }
        )
        SettingsOption(
            RainbowStrings.DefaultUserPostSorting,
            onClick = { onNavigate(Screen.Sheet.UserPostSorting) }
        )
        SettingsOption(
            RainbowStrings.DefaultProfilePostSorting,
            onClick = { onNavigate(Screen.Sheet.UserPostSorting) }
        )
        SettingsOption(
            RainbowStrings.DefaultSubredditPostSorting,
            onClick = { onNavigate(Screen.Sheet.SubredditPostSorting) }
        )
        SettingsOption(
            RainbowStrings.DefaultCommentSorting,
            onClick = { onNavigate(Screen.Sheet.PostCommentSorting) }
        )
        SettingsOption(
            RainbowStrings.DefaultSearchPostSorting,
            onClick = { onNavigate(Screen.Sheet.SearchPostSorting) }
        )
    }
}
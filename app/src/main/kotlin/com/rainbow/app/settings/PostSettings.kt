package com.rainbow.app.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PostSettings(modifier: Modifier = Modifier) {
    SettingsTabContent(modifier) {
        PostFullHeightOption()
        MarkPostAsReadOption()
        PostLayoutOption()
        PostSortingOptions()
    }
}
package com.rainbow.desktop.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.desktop.ui.RainbowTheme

@Composable
fun SettingsTabContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = modifier.padding(RainbowTheme.dpDimensions.medium),
            verticalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium),
            content = content,
        )
    }
}
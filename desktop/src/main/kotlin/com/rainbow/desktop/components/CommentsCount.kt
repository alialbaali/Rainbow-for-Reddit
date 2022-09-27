package com.rainbow.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.format

@Composable
fun CommentsCount(count: Int, modifier: Modifier = Modifier) {
    val formattedCount = remember(count) { count.format() }

    Row(
        modifier
            .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.small)
            .padding(vertical = RainbowTheme.dimensions.small, horizontal = RainbowTheme.dimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium)
    ) {
        Icon(
            RainbowIcons.Forum,
            RainbowStrings.Comments,
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            formattedCount,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}
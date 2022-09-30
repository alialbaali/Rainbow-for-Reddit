package com.rainbow.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.Comments
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.format

@Composable
fun CommentsCount(count: Int, onClick: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    val formattedCount = remember(count) { count.format() }
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier

    Row(
        modifier
            .clip(MaterialTheme.shapes.small)
            .then(clickableModifier)
            .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.small)
            .padding(vertical = RainbowTheme.dimensions.small, horizontal = RainbowTheme.dimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium)
    ) {
        Icon(
            RainbowIcons.Comments,
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
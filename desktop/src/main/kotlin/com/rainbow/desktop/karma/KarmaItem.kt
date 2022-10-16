package com.rainbow.desktop.karma

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.format
import com.rainbow.domain.models.Karma

@Composable
fun KarmaItem(karma: Karma, onClick: (String) -> Unit, modifier: Modifier = Modifier) {
    val postKarma = remember(karma.postKarma) { karma.postKarma.format() }
    val commentKarma = remember(karma.commentKarma) { karma.commentKarma.format() }
    Row(
        modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick(karma.subredditName) }
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .padding(RainbowTheme.dimensions.medium),
        Arrangement.spacedBy(RainbowTheme.dimensions.medium),
        Alignment.CenterVertically,
    ) {
        Text(karma.subredditName, Modifier.weight(1.5F))
        Text(postKarma, Modifier.weight(1F), color = MaterialTheme.colorScheme.surfaceVariant)
        Text(commentKarma, Modifier.weight(1F), color = MaterialTheme.colorScheme.surfaceVariant)
    }
}
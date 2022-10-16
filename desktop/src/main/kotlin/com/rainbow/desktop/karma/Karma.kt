package com.rainbow.desktop.karma

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.domain.models.Karma

fun LazyListScope.karma(
    karma: List<Karma>,
    onSubredditNameClick: (String) -> Unit,
) {
    item {
        Row(
            Modifier
                .fillParentMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                .padding(RainbowTheme.dimensions.medium),
            Arrangement.SpaceBetween,
            Alignment.CenterVertically,
        ) {
            Text(
                RainbowStrings.Subreddit,
                Modifier.weight(1.5F),
                MaterialTheme.colorScheme.onSurface,
            )
            Text(
                RainbowStrings.PostsKarma,
                Modifier.weight(1F),
                MaterialTheme.colorScheme.surfaceVariant,
            )
            Text(
                RainbowStrings.CommentsKarma,
                Modifier.weight(1F),
                MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }

    items(karma) { karmaItem ->
        KarmaItem(karmaItem, onSubredditNameClick, Modifier.fillParentMaxWidth())
    }
}
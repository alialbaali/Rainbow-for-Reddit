package com.rainbow.desktop.award

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.domain.models.Award

@Composable
fun ItemAwards(awards: List<Award>, onClick: () -> Unit, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .clip(CircleShape)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.background, CircleShape)
            .padding(horizontal = RainbowTheme.dpDimensions.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium),
    ) {
        item { Text(awards.sumOf { it.count }.toString()) }
        items(awards) { award ->
            AwardImage(
                award,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}
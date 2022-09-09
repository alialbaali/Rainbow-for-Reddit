package com.rainbow.desktop.award

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.domain.models.Award
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun RowScope.AwardItem(award: Award, modifier: Modifier = Modifier) {
    AwardImage(award, Modifier.size(24.dp))
    Spacer(Modifier.width(16.dp))
    Text(award.name, Modifier.weight(1F))
    Spacer(Modifier.width(16.dp))
    Text(award.count.toString())
}

@Composable
fun AwardImage(award: Award, modifier: Modifier = Modifier) {
    val painterResource = lazyPainterResource(award.imageUrl)
    KamelImage(
        resource = painterResource,
        contentDescription = award.name,
        modifier = modifier,
    )
}
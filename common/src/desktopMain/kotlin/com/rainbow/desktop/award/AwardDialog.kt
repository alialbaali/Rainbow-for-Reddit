package com.rainbow.desktop.award

import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.RainbowDialog
import com.rainbow.domain.models.Award
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun AwardDialog(award: Award, onCloseRequest: () -> Unit, modifier: Modifier = Modifier) {
    val imageResource = lazyPainterResource(award.imageUrl)
    RainbowDialog(onCloseRequest, award.name, modifier) {
        KamelImage(imageResource, award.name, Modifier.size(100.dp))
        Text(award.description)
    }
}
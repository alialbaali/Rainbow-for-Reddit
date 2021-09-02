package com.rainbow.app.award

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.domain.models.Award
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun Award(award: Award, modifier: Modifier = Modifier) {
    val imageResource = lazyPainterResource(award.imageUrl)

    KamelImage(
        resource = imageResource,
        contentDescription = award.name,
        modifier = modifier,
        crossfade = true,
    )
}
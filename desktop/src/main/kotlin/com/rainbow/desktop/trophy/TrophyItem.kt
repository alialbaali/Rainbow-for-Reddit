package com.rainbow.desktop.trophy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.LetterBox
import com.rainbow.desktop.components.LetterBoxSize
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.formatDateOnly
import com.rainbow.domain.models.Trophy
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

private val TrophyImageSize = 50.dp
private fun ImageModifier(backgroundColor: Color = Color.Unspecified) = Modifier.composed {
    Modifier
        .size(TrophyImageSize)
        .clip(MaterialTheme.shapes.small)
        .background(backgroundColor, MaterialTheme.shapes.small)
}

@Composable
fun TrophyItem(trophy: Trophy, modifier: Modifier = Modifier) {
    val imageResource = lazyPainterResource(trophy.imageUrl)
    val grantedAt = remember(trophy.grantedAt) { trophy.grantedAt?.formatDateOnly() }

    Row(
        modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
            .padding(RainbowTheme.dimensions.medium),
        Arrangement.spacedBy(RainbowTheme.dimensions.medium),
        Alignment.CenterVertically,
    ) {
        KamelImage(
            imageResource,
            trophy.name,
            ImageModifier(),
            onFailure = {
                Box(
                    ImageModifier(MaterialTheme.colorScheme.onSurface),
                    contentAlignment = Alignment.Center
                ) {
                    LetterBox(
                        trophy.name,
                        LetterBoxSize.Medium,
                        Modifier.fillMaxSize(),
                        MaterialTheme.colorScheme.surface,
                    )
                }
            },
            onLoading = { RainbowProgressIndicator(ImageModifier()) }
        )

        Text(
            trophy.name,
            Modifier.weight(1F),
        )

        if (grantedAt != null) {
            Text(
                grantedAt,
                color = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}
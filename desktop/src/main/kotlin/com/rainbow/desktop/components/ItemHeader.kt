package com.rainbow.desktop.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.ui.headerImageBorder
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

private val BannerImageModifier = Modifier
    .height(100.dp)
    .fillMaxWidth()

private val BoxScope.ImageModifier
    @Composable
    get() = Modifier
        .padding(top = 50.dp)
        .border(
            RainbowTheme.dimensions.headerImageBorder,
            MaterialTheme.colorScheme.surface,
            MaterialTheme.shapes.medium,
        )
        .size(100.dp)
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.onSurface)
        .align(Alignment.BottomCenter)


@Composable
fun ItemHeader(bannerImageUrl: String, imageUrl: String, text: String, modifier: Modifier = Modifier) {

    val bannerPainterResource = lazyPainterResource(bannerImageUrl)
    val painterResource = lazyPainterResource(imageUrl)
    Box(modifier) {
        KamelImage(
            bannerPainterResource,
            contentDescription = text,
            modifier = BannerImageModifier,
            contentScale = ContentScale.Crop,
            onLoading = { RainbowProgressIndicator(BannerImageModifier) },
            onFailure = {
                Image(
                    ColorPainter(
                        MaterialTheme.colorScheme.onSurface,
                    ),
                    text,
                    BannerImageModifier,
                )
            }
        )

        KamelImage(
            resource = painterResource,
            contentDescription = text,
            modifier = ImageModifier,
            onLoading = { RainbowProgressIndicator(ImageModifier) },
            onFailure = { LetterBox(text, LetterBoxSize.Large, ImageModifier) }
        )
    }
}
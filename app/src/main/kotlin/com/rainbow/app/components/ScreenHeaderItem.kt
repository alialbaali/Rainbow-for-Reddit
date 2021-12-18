package com.rainbow.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.utils.ImageBorderSize
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding
import com.rainbow.domain.models.User
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource


@Composable
fun ScreenHeaderItem(
    bannerImageUrl: String,
    imageUrl: String,
    text: String,
    modifier: Modifier = Modifier,
    imageShape: Shape = CircleShape,
) {
    val bannerImageResource = lazyPainterResource(bannerImageUrl)
    val profileImageResource = lazyPainterResource(imageUrl)
    val bannerImageHeight = 200.dp
    val profileImageSize = 200.dp
    val bannerImageGradientHeight = bannerImageHeight / 2.dp
    val bannerImageGradient = Brush.verticalGradient(
        0.1F to Color.Transparent,
        1.0F to Color.Black,
        startY = bannerImageGradientHeight
    )

    val BannerImageModifier = Modifier
        .fillMaxWidth()
        .height(bannerImageHeight)
        .drawWithContent {
            drawContent()
            drawRect(
                bannerImageGradient,
                topLeft = Offset(0F, bannerImageGradientHeight)
            )
        }

    val ImageModifier = Modifier
        .padding(start = 16.dp)
        .size(profileImageSize)
        .offset(y = 100.dp)
        .graphicsLayer {
            clip = true
            shape = imageShape
        }
        .border(ImageBorderSize, MaterialTheme.colors.surface, imageShape)

    Box(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        KamelImage(
            resource = bannerImageResource,
            contentDescription = text,
            modifier = BannerImageModifier,
            contentScale = ContentScale.Crop,
            crossfade = true,
            onLoading = { RainbowProgressIndicator(BannerImageModifier) },
            onFailure = {
                Image(
                    ColorPainter(MaterialTheme.colors.primary),
                    text,
                    BannerImageModifier,
                )
            }
        )

        KamelImage(
            profileImageResource,
            contentDescription = text,
            modifier = ImageModifier,
            contentScale = ContentScale.Fit,
            crossfade = true,
            onLoading = { RainbowProgressIndicator(ImageModifier) },
            onFailure = { TextBox(text, 180.sp, ImageModifier.background(MaterialTheme.colors.secondary)) }
        )

        Text(
            text,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.background,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .defaultPadding(start = 232.dp)
        )
    }
}

@Composable
fun HeaderDescription(user: User, modifier: Modifier = Modifier) {
    Text(
        text = user.description ?: RainbowStrings.EmptyDescription,
        color = MaterialTheme.colors.onBackground,
        modifier = modifier,
        style = MaterialTheme.typography.h6,
    )
}
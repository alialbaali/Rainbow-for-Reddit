package com.rainbow.desktop.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.desktop.utils.ImageBorderSize
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
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
    val bannerImageResource = lazyPainterResource(bannerImageUrl, filterQuality = FilterQuality.High)
    val profileImageResource = lazyPainterResource(imageUrl, filterQuality = FilterQuality.High)
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
        .border(ImageBorderSize, MaterialTheme.colorScheme.surface, imageShape)
        .background(MaterialTheme.colorScheme.surface, imageShape)

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
            onLoading = { RainbowProgressIndicator(BannerImageModifier) },
            onFailure = {
                Image(
                    ColorPainter(MaterialTheme.colorScheme.primary),
                    text,
                    BannerImageModifier,
                )
            },
            animationSpec = tween(),
        )

        KamelImage(
            profileImageResource,
            contentDescription = text,
            modifier = ImageModifier,
            contentScale = ContentScale.Fit,
            onLoading = { RainbowProgressIndicator(ImageModifier) },
            onFailure = { TextBox(text, 180.sp, ImageModifier.background(MaterialTheme.colorScheme.secondary)) },
            animationSpec = tween(),
        )

        Text(
            text,
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
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
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier,
        style = MaterialTheme.typography.headlineLarge,
    )
}
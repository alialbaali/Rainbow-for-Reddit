package com.rainbow.desktop.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.ui.headerImageBorder
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.desktop.utils.formatDateOnly
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource
import kotlinx.datetime.Instant

private val BannerImageHeight = 200.dp
private val ProfileImageSize = 200.dp
private val BannerImageGradientHeight = BannerImageHeight / 2.dp
private fun ImageModifier(shape: Shape) = Modifier
    .padding(start = 16.dp)
    .size(ProfileImageSize)
    .offset(y = 100.dp)
    .composed {
        Modifier
            .clip(shape)
            .border(RainbowTheme.dimensions.headerImageBorder, MaterialTheme.colorScheme.surface, shape)
            .background(MaterialTheme.colorScheme.inverseSurface, shape)
    }

private val ScreenHeaderHeight = BannerImageHeight + (ProfileImageSize / 2)
private val ContentHeight = 100.dp
val ScreenHeaderContentMinHeight = ScreenHeaderHeight + ContentHeight

private fun BannerImageModifier(brush: Brush) = Modifier
    .fillMaxWidth()
    .height(BannerImageHeight)
    .drawWithContent {
        drawContent()
        drawRect(
            brush,
            topLeft = Offset(0F, BannerImageGradientHeight)
        )
    }

@Composable
fun ScreenHeader(
    bannerImageUrl: String,
    imageUrl: String,
    title: String,
    modifier: Modifier = Modifier,
    imageShape: Shape = CircleShape,
    content: @Composable RowScope.() -> Unit = {}
) {
    val bannerImageResource = lazyPainterResource(bannerImageUrl, filterQuality = FilterQuality.High)
    val profileImageResource = lazyPainterResource(imageUrl, filterQuality = FilterQuality.High)
    val bannerImageGradient = remember(BannerImageGradientHeight) {
        Brush.verticalGradient(
            0.1F to Color.Transparent,
            1.0F to Color.Black,
            startY = BannerImageGradientHeight
        )
    }

    Box(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        KamelImage(
            resource = bannerImageResource,
            contentDescription = title,
            modifier = BannerImageModifier(bannerImageGradient),
            contentScale = ContentScale.Crop,
            onLoading = { RainbowProgressIndicator(BannerImageModifier(bannerImageGradient)) },
            onFailure = {
                Image(
                    ColorPainter(MaterialTheme.colorScheme.primary),
                    title,
                    BannerImageModifier(bannerImageGradient),
                )
            },
            animationSpec = tween(),
        )

        KamelImage(
            profileImageResource,
            contentDescription = title,
            modifier = ImageModifier(imageShape),
            contentScale = ContentScale.Fit,
            onLoading = { RainbowProgressIndicator(ImageModifier(imageShape)) },
            onFailure = { LetterBox(title, LetterBoxSize.ExtraLarge, ImageModifier(imageShape)) },
            animationSpec = tween(),
        )

        CompositionLocalProvider(LocalContentColor provides Color.White) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .defaultPadding(start = 232.dp),
            ) {
                SelectionContainer {
                    Text(
                        text = title,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.displayMedium,
                    )
                }
                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyLarge) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.medium),
                        verticalAlignment = Alignment.CenterVertically,
                        content = content,
                    )
                }
            }
        }
    }
}

@Composable
fun ScreenHeaderCreationDate(date: Instant, icon: ImageVector = RainbowIcons.Schedule, modifier: Modifier = Modifier) {
    val formattedDate = remember(date) { date.formatDateOnly() }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dimensions.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, RainbowStrings.Created)
        Text(formattedDate)
    }
}

@Composable
fun ScreenHeaderDescription(description: String?, modifier: Modifier = Modifier) {
    SelectionContainer {
        ExpandableText(description ?: RainbowStrings.EmptyDescription, modifier)
    }
}
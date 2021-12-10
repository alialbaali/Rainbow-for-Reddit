package com.rainbow.app.user

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.components.TextImage
import com.rainbow.app.utils.ImageBorderSize
import com.rainbow.domain.models.User
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

private val BannerImageModifier = Modifier
    .height(100.dp)
    .fillMaxWidth()

private val BoxScope.ImageModifier
    @Composable
    get() = Modifier
        .padding(top = 50.dp)
        .border(ImageBorderSize, MaterialTheme.colors.background, CircleShape)
        .size(100.dp)
        .clip(CircleShape)
        .align(Alignment.BottomCenter)

@Composable
fun UserItemName(userName: String, modifier: Modifier = Modifier) {
    Text(
        text = userName,
        modifier = modifier,
        fontSize = 26.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.onBackground
    )
}

@Composable
fun UserItemHeader(user: User) {
    val bannerPainterResource = lazyPainterResource(user.bannerImageUrl.toString())
    val painterResource = lazyPainterResource(user.imageUrl.toString())
    Box {

        KamelImage(
            bannerPainterResource,
            contentDescription = user.name,
            modifier = BannerImageModifier,
            contentScale = ContentScale.Crop,
            onLoading = { RainbowProgressIndicator(BannerImageModifier) },
            onFailure = {
                Image(
                    ColorPainter(
                        MaterialTheme.colors.primary,
                    ),
                    user.name,
                    BannerImageModifier,
                )
            }
        )

        KamelImage(
            resource = painterResource,
            contentDescription = user.name,
            modifier = ImageModifier,
            onLoading = { RainbowProgressIndicator(ImageModifier) },
            onFailure = { TextImage(user.name, ImageModifier.background(MaterialTheme.colors.secondary)) }
        )
    }
}
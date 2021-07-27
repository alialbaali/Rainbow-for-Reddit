package com.rainbow.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.rainbow.app.comment.RainbowProgressIndicator
import com.rainbow.app.utils.shadow
import io.kamel.core.Resource
import io.kamel.image.KamelImage

@Composable
fun BannerImage(resource: Resource<ImageBitmap>, bannerColor: Color, modifier: Modifier = Modifier) {
    KamelImage(
        resource,
        contentDescription = null,
        modifier = modifier
            .background(bannerColor),
        contentScale = ContentScale.Crop,
        crossfade = true,
        onLoading = { RainbowProgressIndicator(modifier) }
    )
}

@Composable
fun ProfileImage(resource: Resource<ImageBitmap>, primaryColor: Color, modifier: Modifier = Modifier) {
    KamelImage(
        resource,
        contentDescription = null,
        modifier = modifier
            .graphicsLayer {
                clip = true
                shape = CircleShape
                shadow = 12.dp
            }
            .background(Color.White),
        contentScale = ContentScale.Fit,
        crossfade = true,
    )
}
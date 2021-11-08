package com.rainbow.app.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asComposeImageBitmap
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Codec
import org.jetbrains.skia.Data
import java.net.URL

@Composable
fun Gif(url: String, contentDescription: String, modifier: Modifier = Modifier) {
    val codec = remember {
        val bytes = URL(url).readBytes()
        Codec.makeFromData(Data.makeFromBytes(bytes))
    }
    val transition = rememberInfiniteTransition()
    val frameIndex by transition.animateValue(
        initialValue = 0,
        targetValue = codec.frameCount - 1,
        Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 0
                for ((index, frame) in codec.framesInfo.withIndex()) {
                    index at durationMillis
                    durationMillis += frame.duration
                }
            }
        )
    )

    val bitmap = remember { Bitmap().apply { allocPixels(codec.imageInfo) } }
    codec.readPixels(bitmap, frameIndex)
    Image(bitmap.asComposeImageBitmap(), contentDescription, modifier)
}
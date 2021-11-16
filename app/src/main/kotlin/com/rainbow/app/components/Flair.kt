package com.rainbow.app.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.domain.models.Flair
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun Flair(flair: Flair, textColor: Flair.TextColor, modifier: Modifier = Modifier) {
    when (flair) {
        is Flair.ImageFlair -> ImageFlair(flair, modifier)
        is Flair.TextFlair -> TextFlair(flair, textColor, modifier)
    }
}

@Composable
private fun ImageFlair(flair: Flair.ImageFlair, modifier: Modifier = Modifier) {
    val painterResource = lazyPainterResource(flair.url)
    KamelImage(painterResource, contentDescription = null, modifier.size(20.dp))
}

@Composable
private fun TextFlair(flair: Flair.TextFlair, textColor: Flair.TextColor, modifier: Modifier = Modifier) {
    Text(
        flair.text,
        modifier,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = when (textColor) {
            Flair.TextColor.Light -> MaterialTheme.colors.background
            Flair.TextColor.Dark -> MaterialTheme.colors.onBackground
        },
        textAlign = TextAlign.Center
    )
}
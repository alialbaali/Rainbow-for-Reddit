package com.rainbow.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.domain.models.Flair
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@Composable
fun Flair(flair: Flair, modifier: Modifier = Modifier) {
    when (flair) {
        is Flair.ImageFlair -> ImageFlair(flair, modifier)
        is Flair.TextFlair -> TextFlair(flair, modifier)
    }
}

@Composable
private fun ImageFlair(flair: Flair.ImageFlair, modifier: Modifier = Modifier) {
    Row(
        modifier
            .background(Color(flair.backgroundColor), CircleShape)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        flair.urls.forEach { url ->
            val painterResource = lazyPainterResource(url)
            KamelImage(painterResource, contentDescription = null)
        }
    }
}

@Composable
private fun TextFlair(flair: Flair.TextFlair, modifier: Modifier = Modifier) {
    Text(
        flair.text,
        modifier
            .background(Color(flair.backgroundColor), CircleShape)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = when (flair.textColor) {
            Flair.TextFlair.TextColor.Light -> MaterialTheme.colors.background
            Flair.TextFlair.TextColor.Dark -> MaterialTheme.colors.onBackground
        },
        textAlign = TextAlign.Center
    )
}
package com.rainbow.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun FlairItem(flair: Flair, modifier: Modifier = Modifier) {
    Row(
        modifier
            .background(Color(flair.backgroundColor), CircleShape)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        flair.types.forEach { type ->
            when (type) {
                is Flair.Type.Image -> ImageFlairType(type)
                is Flair.Type.Text -> TextFlairType(type, flair.textColor)
            }
        }
    }
}

@Composable
private fun ImageFlairType(type: Flair.Type.Image, modifier: Modifier = Modifier) {
    val painterResource = lazyPainterResource(type.url)
    KamelImage(painterResource, contentDescription = null, modifier.size(20.dp))
}

@Composable
private fun TextFlairType(type: Flair.Type.Text, textColor: Flair.TextColor, modifier: Modifier = Modifier) {
    Text(
        type.text,
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
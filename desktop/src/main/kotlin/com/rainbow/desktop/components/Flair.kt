package com.rainbow.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.domain.models.Flair
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

enum class FlairType {
    User,
    Post,
}

@Composable
fun FlairItem(
    flair: Flair,
    type: FlairType,
    modifier: Modifier = Modifier,
) {
    val horizontalPadding = when (type) {
        FlairType.User -> RainbowTheme.dpDimensions.extraSmall
        FlairType.Post -> RainbowTheme.dpDimensions.small
    }
    val verticalPadding = when (type) {
        FlairType.User -> 2.dp
        FlairType.Post -> RainbowTheme.dpDimensions.extraSmall
    }
    val textStyle = when (type) {
        FlairType.User -> MaterialTheme.typography.labelMedium
        FlairType.Post -> MaterialTheme.typography.labelLarge
    }
    Row(
        modifier
            .background(Color(flair.backgroundColor), MaterialTheme.shapes.extraSmall)
            .padding(horizontalPadding, verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        flair.types.forEach { type ->
            when (type) {
                is Flair.Type.Image -> ImageFlairType(type)
                is Flair.Type.Text -> TextFlairType(type, flair.textColor, textStyle)
            }
        }
    }
}

@Composable
private fun ImageFlairType(
    type: Flair.Type.Image,
    modifier: Modifier = Modifier
) {
    val painterResource = lazyPainterResource(type.url)
    KamelImage(painterResource, contentDescription = null, modifier.size(20.dp))
}

@Composable
private fun TextFlairType(
    type: Flair.Type.Text,
    textColor: Flair.TextColor,
    style: TextStyle,
    modifier: Modifier = Modifier
) {
    Text(
        type.text,
        modifier,
        color = when (textColor) {
            Flair.TextColor.Light -> Color.White
            Flair.TextColor.Dark -> Color.Black
        },
        style = style
    )
}
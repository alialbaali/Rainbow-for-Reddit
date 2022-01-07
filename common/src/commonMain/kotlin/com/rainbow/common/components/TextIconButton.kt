package com.rainbow.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextIconButton(
    text: String,
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    shape: Shape = CircleShape,
) {
    Button(
        onClick,
        modifier,
        colors = ButtonDefaults.buttonColors(
            MaterialTheme.colors.background,
            MaterialTheme.colors.onBackground,
        ),
        elevation = null,
        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground.copy(0.1F)),
        shape = shape,
        contentPadding = PaddingValues(16.dp, 12.dp)
    ) {
        Text(text = text, fontSize = 14.sp)
        Spacer(Modifier.width(16.dp))
        Icon(imageVector, contentDescription, iconModifier)
    }
}
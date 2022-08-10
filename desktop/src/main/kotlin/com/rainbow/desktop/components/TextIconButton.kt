package com.rainbow.desktop.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectionButton(
    expanded: Boolean,
    text: String,
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val iconRotation by animateFloatAsState(if (expanded) 180F else 0F)
    Button(
        onClick,
        modifier,
        enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor =  MaterialTheme.colorScheme.onSurface,
        )
    ) {
        Text(text = text, fontSize = 14.sp, modifier = Modifier.alignByBaseline())
        Spacer(Modifier.width(16.dp))
        Icon(imageVector, text, Modifier.rotate(iconRotation).alignByBaseline())
    }
}
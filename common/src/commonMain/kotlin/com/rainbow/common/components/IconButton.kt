package com.rainbow.common.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings
import com.rainbow.common.utils.defaultBackgroundShape

@Composable
inline fun RainbowIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    crossinline onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = { onClick() },
        modifier.defaultBackgroundShape(shape = CircleShape),
        enabled
    ) {
        Icon(imageVector, contentDescription)
    }
}

@Composable
inline fun MenuIconButton(crossinline onClick: () -> Unit, modifier: Modifier = Modifier) {
    RainbowIconButton(
        RainbowIcons.MoreVert,
        RainbowStrings.Menu,
        onClick,
        modifier
    )
}
package com.rainbow.common.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings
import com.rainbow.common.utils.defaultBackgroundShape

@Composable
fun RainbowIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IconButton(onClick, modifier.defaultBackgroundShape(shape = CircleShape), enabled) {
        Icon(imageVector, contentDescription)
    }
}

@Composable
fun MenuIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    RainbowIconButton(
        RainbowIcons.MoreVert,
        RainbowStrings.Menu,
        onClick,
        modifier
    )
}
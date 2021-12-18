package com.rainbow.app.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.RainbowIcons
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultBackgroundShape

@Composable
fun MenuIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick, modifier.defaultBackgroundShape(shape = CircleShape)) {
        Icon(RainbowIcons.MoreVert, contentDescription = RainbowStrings.Menu)
    }
}
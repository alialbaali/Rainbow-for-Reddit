package com.rainbow.desktop.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import com.rainbow.desktop.utils.RainbowStrings

@Composable
fun LogoutButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
        border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(MaterialTheme.colorScheme.error)),
    ) {
        Text(RainbowStrings.Logout)
    }
}
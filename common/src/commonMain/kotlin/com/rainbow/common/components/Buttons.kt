package com.rainbow.common.components

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import com.rainbow.common.utils.RainbowStrings

@Composable
fun LogoutButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.error),
        border = ButtonDefaults.outlinedBorder.copy(brush = SolidColor(MaterialTheme.colors.error)),
    ) {
        Text(RainbowStrings.Logout)
    }
}
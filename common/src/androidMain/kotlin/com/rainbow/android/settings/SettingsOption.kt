package com.rainbow.android.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsOption(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Text(text, modifier.clickable(onClick = onClick).padding(16.dp))
}
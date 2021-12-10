package com.rainbow.app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding
import com.rainbow.data.Repos
import kotlinx.coroutines.launch

@Composable
fun PostFullHeightOption(modifier: Modifier = Modifier) {

    val isFulLHeight by SettingsModel.isPostFullHeight.collectAsState()
    val scope = rememberCoroutineScope()

    Row(
        modifier
            .fillMaxWidth()
            .defaultPadding(),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically,
    ) {

        Text(RainbowStrings.PostFullHeight)

        Switch(
            isFulLHeight,
            onCheckedChange = {
                scope.launch {
                    Repos.Settings.setIsFullHeight(it)
                }
            },
        )
    }

}
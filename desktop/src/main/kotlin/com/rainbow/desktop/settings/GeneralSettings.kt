package com.rainbow.desktop.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.rainbow.desktop.components.EnumDropdownMenuHolder
import com.rainbow.desktop.components.RainbowButton
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding

@Composable
fun GeneralSettings(modifier: Modifier = Modifier) {
    SettingsTabContent(modifier) {
        ThemeOption()
        TextSelectionOption()
        LogoutButtonWithDialog()
    }
}

@Composable
private fun TextSelectionOption(modifier: Modifier = Modifier) {
    val isTextSelectionEnabled by SettingsStateHolder.isTextSelectionEnabled.collectAsState()
    SettingsOption(RainbowStrings.TextSelection) {
        Switch(
            isTextSelectionEnabled,
            SettingsStateHolder::setIsTextSelectionEnabled,
        )
    }
}


@Composable
private fun ThemeOption(modifier: Modifier = Modifier) {
    val theme by SettingsStateHolder.theme.collectAsState()
    SettingsOption(RainbowStrings.Theme, modifier) {
        EnumDropdownMenuHolder(
            theme,
            SettingsStateHolder::setTheme,
            containerColor = MaterialTheme.colorScheme.background,
        )
    }
}

@Composable
private fun LogoutButtonWithDialog(modifier: Modifier = Modifier) {
    var isDialogShown by remember { mutableStateOf(false) }
    RainbowButton(
        onClick = { isDialogShown = true },
        containerColor = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
    ) {
        Text(RainbowStrings.Logout, style = MaterialTheme.typography.titleMedium)
    }
    if (isDialogShown) {
        LogoutDialog(onCloseRequest = { isDialogShown = false })
    }
}

@Composable
private fun LogoutDialog(
    onCloseRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onCloseRequest = onCloseRequest,
        title = RainbowStrings.Logout,
        resizable = false,
    ) {
        Column(
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                RainbowStrings.LogoutConfirmation,
                Modifier.defaultPadding(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
            )

            Row(
                Modifier
                    .defaultPadding()
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(RainbowTheme.dpDimensions.medium)
            ) {
                RainbowButton(
                    onClick = SettingsStateHolder::logoutUser,
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ) {
                    Text(RainbowStrings.Logout)
                }
                RainbowButton(
                    onCloseRequest,
                ) {
                    Text(RainbowStrings.Cancel)
                }
            }
        }
    }
}
package com.rainbow.desktop.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rainbow.desktop.components.LogoutButton
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.desktop.components.DropdownMenuHolder

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
        DropdownMenuHolder(theme, SettingsStateHolder::setTheme)
    }
}

@Composable
private fun LogoutButtonWithDialog(modifier: Modifier = Modifier) {
    var isDialogShown by remember { mutableStateOf(false) }
    LogoutButton(onClick = { isDialogShown = true })
    if (isDialogShown)
        LogoutDialog(onCloseRequest = { isDialogShown = false })
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
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                RainbowStrings.LogoutConfirmation,
                Modifier.defaultPadding(),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge,
            )

            Row(
                Modifier
                    .defaultPadding()
                    .align(Alignment.End)
            ) {
                Button(
                    onClick = { SettingsStateHolder.logoutUser() },
                    modifier = Modifier
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                ) {
                    Text(RainbowStrings.Logout)
                }

                OutlinedButton(
                    onClick = onCloseRequest,
                    modifier = Modifier
                        .padding(start = 8.dp),
                ) {
                    Text(RainbowStrings.Cancel)
                }
            }
        }
    }
}
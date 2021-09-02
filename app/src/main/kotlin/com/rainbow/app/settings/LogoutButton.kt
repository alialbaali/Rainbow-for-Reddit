package com.rainbow.app.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding
import com.rainbow.data.Repos
import kotlinx.coroutines.launch

@Composable
fun LogoutButton() {
    var isDialogShown by remember { mutableStateOf(false) }

    OutlinedButton(
        onClick = { isDialogShown = true },
        modifier = Modifier.defaultPadding(),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colors.error),
        border = ButtonDefaults.outlinedBorder.copy(brush = SolidColor(MaterialTheme.colors.error)),
    ) {
        Text(RainbowStrings.Logout)
    }

    if (isDialogShown)
        LogoutDialog(onCloseRequest = { isDialogShown = false })
}

@Composable
private fun LogoutDialog(
    onCloseRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    Dialog(
        onCloseRequest = onCloseRequest,
        title = RainbowStrings.Logout,
        resizable = false,
    ) {
        Column(
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                RainbowStrings.LogoutConfirmation,
                Modifier.defaultPadding(),
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h5,
            )

            Row(
                Modifier
                    .defaultPadding()
                    .align(Alignment.End)
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            Repos.User.logoutUser()
                        }
                    },
                    modifier = Modifier
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error),
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
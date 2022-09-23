package com.rainbow.desktop.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.rainbow.desktop.components.RainbowButton
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.defaultPadding

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val stateHolder = remember { LoginScreenStateHolder.Instance }
    val state by stateHolder.state.collectAsState()
    val currentUriHandler = LocalUriHandler.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        if (state.isFailure) {
            snackbarHostState.showSnackbar(RainbowStrings.LoginFailed)
        }
    }
    Box(modifier, contentAlignment = Alignment.Center) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(RainbowStrings.Intro, style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(RainbowTheme.dimensions.extraLarge))
            RainbowButton(
                onClick = {
                    val url = stateHolder.createAuthenticationUrl()
                    currentUriHandler.openUri(url)
                    stateHolder.loginUser()
                },
                enabled = state !is UIState.Loading,
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(RainbowStrings.Login)
            }
        }

        SnackbarHost(
            snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .defaultPadding()
        )
    }
}

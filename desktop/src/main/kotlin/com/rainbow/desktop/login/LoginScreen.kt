package com.rainbow.desktop.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.settings.SettingsStateHolder
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.UIState
import com.rainbow.desktop.utils.defaultPadding
import io.ktor.http.*
import java.util.*

@Composable
fun LoginScreen() {

    var state by remember { mutableStateOf<UIState<Unit>?>(null) }
    var isButtonEnabled by remember { mutableStateOf(true) }
    var isSnackbarEnabled by remember { mutableStateOf(false) }
    val currentUriHandler = LocalUriHandler.current

    if (isSnackbarEnabled) {
        val snackbarHostState = remember { SnackbarHostState() }

        SnackbarHost(snackbarHostState, modifier = Modifier.defaultPadding())

        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar("Login failed")
        }
    }

    when (state) {
        is UIState.Loading -> RainbowProgressIndicator()
        else -> {
            Column(
                Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(RainbowStrings.Intro, style = MaterialTheme.typography.headlineLarge)

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        state = UIState.Loading
                        val uuid = UUID.randomUUID()
                        currentUriHandler.openUri(createAuthenticationUrl(uuid))
                        SettingsStateHolder.loginUser(
                            uuid,
                            onSuccess = { state = UIState.Success(Unit) },
                            onFailure = {
                                state = UIState.Failure(it)
                                isButtonEnabled = true
                                isSnackbarEnabled = true
                            }
                        )
                        isButtonEnabled = false
                    },
                    enabled = isButtonEnabled,
                ) {
                    Text(RainbowStrings.Login)
                }
            }
        }
    }
}

private fun createAuthenticationUrl(uuid: UUID): String {
    return URLBuilder(
        protocol = URLProtocol.HTTPS,
        host = "www.reddit.com/api/v1/authorize",
        parameters = ParametersBuilder().apply {
            append("client_id", "cpKMrRbh8b06TQ")
            append("response_type", "code")
            append("state", uuid.toString())
            append("redirect_uri", "https://rainbowforreddit.herokuapp.com/")
            append("duration", "permanent")
            append(
                "scope",
                "submit, vote, mysubreddits, privatemessages, subscribe, history, wikiread, flair, identity, edit, read, report, save, submit"
            )
        }.build()
    ).buildString()
}

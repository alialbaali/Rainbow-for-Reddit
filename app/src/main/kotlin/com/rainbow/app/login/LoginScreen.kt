package com.rainbow.app.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
//import androidx.compose.ui.platform.LocalUriHandler
import com.rainbow.app.components.RainbowProgressIndicator
import com.rainbow.app.ui.dpDimensions
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.defaultPadding
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import io.ktor.http.*
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun LoginScreen() {

    var state by remember { mutableStateOf<UIState<Unit>?>(null) }
    var isButtonEnabled by remember { mutableStateOf(true) }
    var isSnackbarEnabled by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
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
                Text(RainbowStrings.Intro, style = MaterialTheme.typography.h4)

                Spacer(Modifier.height(MaterialTheme.dpDimensions.extraLarge))

                Button(
                    onClick = {
                        state = UIState.Loading
                        val uuid = UUID.randomUUID()
                        currentUriHandler.openUri(createAuthenticationUrl(uuid))
                        scope.launch {
                            state = Repos.User.loginUser(uuid)
                                .onFailure {
                                    isButtonEnabled = true
                                    isSnackbarEnabled = true
                                }
                                .toUIState()
                        }
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
            append("redirect_uri", "http://localhost:8080")
            append("duration", "permanent")
            append(
                "scope",
                "submit, vote, mysubreddits, privatemessages, subscribe, history, wikiread, flair, identity, edit, read, report, save, submit"
            )
        }
    ).buildString()
}

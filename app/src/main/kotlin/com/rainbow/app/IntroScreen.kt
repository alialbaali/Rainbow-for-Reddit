package com.rainbow.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun IntroScreen(modifier: Modifier = Modifier) {

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Text("Welcome to Rainbow", style = MaterialTheme.typography.h3)

        Spacer(Modifier.height(16.dp))

        Text("3rd Party Reddit Client", style = MaterialTheme.typography.subtitle2)

        Spacer(Modifier.height(32.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Button(
                {},
            ) {
                Text(
                    "Sign in",
                    Modifier.padding(8.dp),
                    style = MaterialTheme.typography.h6,
                )
            }

            Spacer(Modifier.width(16.dp))

            Button(
                {},
            ) {
                Text(
                    "Sign up",
                    Modifier.padding(8.dp),
                    style = MaterialTheme.typography.h6
                )
            }

        }

    }

}
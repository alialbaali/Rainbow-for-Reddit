package com.rainbow.desktop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.ui.RainbowTheme
import com.rainbow.desktop.utils.RainbowStrings
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    var isTextVisible by remember { mutableStateOf(false) }

    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource("icons/Rainbow.svg"),
            contentDescription = RainbowStrings.Rainbow,
            modifier = Modifier.size(300.dp),
        )
        Spacer(Modifier.height(RainbowTheme.dimensions.extraLarge))
        AnimatedVisibility(isTextVisible) {
            Text(RainbowStrings.Rainbow, style = MaterialTheme.typography.displayLarge)
        }
    }

    LaunchedEffect(Unit) {
        delay(100)
        isTextVisible = true
    }
}
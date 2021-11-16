package com.rainbow.app.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding
import com.rainbow.data.Repos
import com.rainbow.domain.models.PostLayout
import kotlinx.coroutines.launch

@Composable
fun PostLayoutOption(modifier: Modifier = Modifier) {

    val scope = rememberCoroutineScope()
    val currentPostLayout by Repos.Settings.postLayout.collectAsState(PostLayout.Card)

    Row(
        modifier
            .fillMaxWidth()
            .defaultPadding(),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically,
    ) {

        Text(
            RainbowStrings.PostLayout,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body1,
        )

        Row {
            PostLayout.values().forEach { postLayout ->

                val isSelected = currentPostLayout == postLayout

                Button(
                    onClick = {
                        scope.launch {
                            Repos.Settings.setPostLayout(postLayout)
                        }
                    },
                    modifier = Modifier.padding(horizontal = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isSelected)
                            MaterialTheme.colors.primary
                        else
                            MaterialTheme.colors.background
                    )
                ) {
                    Text(
                        postLayout.name,
                        color = if (isSelected)
                            MaterialTheme.colors.onPrimary
                        else
                            MaterialTheme.colors.onBackground,
                        style = MaterialTheme.typography.body1
                    )
                }
            }
        }
    }

}
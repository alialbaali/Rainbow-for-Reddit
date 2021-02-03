package com.rainbow.app.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.defaultPadding

@Composable
fun Search(modifier: Modifier = Modifier) {

    var searchTerm by remember { mutableStateOf("") }

    BasicTextField(
        searchTerm,
        onValueChange = { searchTerm = it },
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.onSurface.copy(0.05F))
            .padding(12.dp)
        ,
//        placeholder = {
//            Text("Search for Users, Subreddits or Posts...")
//        }
    )

}
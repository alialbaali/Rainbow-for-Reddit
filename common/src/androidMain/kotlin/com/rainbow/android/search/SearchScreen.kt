package com.rainbow.android.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.rainbow.common.components.RainbowTextField
import com.rainbow.common.utils.RainbowStrings

@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    var query by remember { mutableStateOf("") }
    Column(modifier) {
        RainbowTextField(
            query,
            onValueChange = { query = it },
            placeholderText = RainbowStrings.Search
        )

        LazyColumn(modifier) {

        }
    }
}

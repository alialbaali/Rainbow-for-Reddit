package com.rainbow.app.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rainbow.app.utils.UIState
import com.rainbow.app.utils.toUIState
import com.rainbow.data.Repos
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.SubredditsSearchSorting
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@Composable
fun Search(modifier: Modifier = Modifier) {

    var searchTerm by remember { mutableStateOf("") }

    var isExpanded by remember { mutableStateOf(false) }

    val state by produceState<UIState<List<Subreddit>>>(UIState.Loading, searchTerm) {
        Repos.SubredditRepo.searchSubreddit(searchTerm, SubredditsSearchSorting.Activity)
            .map { it.toUIState() }
            .collect { value = it }
    }

    BasicTextField(
        searchTerm,
        onValueChange = { searchTerm = it },
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.onSurface.copy(0.05F))
            .padding(12.dp),
    )

    when (state) {
        is UIState.Loading -> {

        }
        is UIState.Success -> {
            isExpanded = true
            DropdownMenu(isExpanded, onDismissRequest = { isExpanded = false }) {
                (state as UIState.Success<List<Subreddit>>).value.onEach {
                    DropdownMenuItem({}) {
                        Text(it.name)
                    }
                }
            }
        }
        is UIState.Failure -> {

        }
    }

}
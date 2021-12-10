package com.rainbow.app.user

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp
import com.rainbow.app.subreddit.SubredditType
import com.rainbow.app.utils.RainbowStrings
import com.rainbow.app.utils.defaultPadding
import com.rainbow.domain.models.User

enum class UserType { Default, Search }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Users(
    users: List<User>,
    userType: UserType,
    onClick: (User) -> Unit,
    onLoadMore: (User) -> Unit,
    onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var cellCount by remember { mutableStateOf(3) }
    val scrollingState = rememberLazyListState()
    var searchTerm by remember { mutableStateOf("") }
    val subreddits = users.let {
        searchTerm.filter { !it.isWhitespace() }
            .takeIf { it.isNotBlank() }
            ?.let { searchTerm ->
                users.filter {
                    it.name.contains(searchTerm, ignoreCase = true) || it.description.orEmpty().contains(searchTerm)
                }
            } ?: users
    }

    LazyColumn(
        modifier
            .padding(horizontal = 16.dp)
            .onSizeChanged { size ->
                cellCount = when {
                    size.width < 1000 -> 2
                    size.width in 1000 until 1500 -> 3
                    else -> 4
                }
            },
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = scrollingState,
    ) {
        val rows = if (subreddits.isEmpty()) 0 else 1 + (subreddits.size - 1) / cellCount
        item {
            OutlinedTextField(
                value = searchTerm,
                onValueChange = { searchTerm = it },
                modifier = Modifier.defaultPadding(),
                placeholder = { Text(RainbowStrings.FilterSubreddits) },
            )
        }
        items(rows) { rowIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                for (columnIndex in 0 until cellCount) {
                    val itemIndex = rowIndex * cellCount + columnIndex
                    if (itemIndex < subreddits.size) {
                        Box(
                            modifier = Modifier.weight(1f, fill = true),
                            propagateMinConstraints = true
                        ) {
                            when (userType) {
                                UserType.Default -> UserItem(subreddits[itemIndex], onClick, onShowSnackbar)
                                UserType.Search -> UserItem(
                                    subreddits[itemIndex],
                                    onClick,
                                    onShowSnackbar
                                )
                            }
                        }
                    } else {
                        Spacer(Modifier.weight(1f, fill = true))
                    }
                }
            }
        }
    }
    VerticalScrollbar(rememberScrollbarAdapter(scrollingState))
}

package com.rainbow.desktop.comment

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.rainbow.desktop.components.RainbowProgressIndicator
import com.rainbow.desktop.navigation.ContentScreen
import com.rainbow.desktop.navigation.Screen
import com.rainbow.desktop.utils.UIState
import com.rainbow.domain.models.Comment

inline fun LazyListScope.comments(
    commentsState: UIState<List<Comment>>,
    crossinline onNavigate: (Screen) -> Unit,
    crossinline onNavigateContentScreen: (ContentScreen) -> Unit,
) {
    when (commentsState) {
        is UIState.Failure -> item { Text("Failed loading comments") }
        is UIState.Loading -> item { RainbowProgressIndicator() }
        is UIState.Success -> itemsIndexed(commentsState.data) { index, comment ->
            CommentItem(
                comment,
                onNavigate,
                onNavigateContentScreen,
                modifier = Modifier.fillParentMaxWidth()
            )
        }
        is UIState.Empty -> {}
    }
}
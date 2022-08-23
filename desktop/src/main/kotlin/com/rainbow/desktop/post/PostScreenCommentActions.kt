package com.rainbow.desktop.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.rainbow.desktop.components.DropdownMenuHolder
import com.rainbow.desktop.components.RainbowIconButton
import com.rainbow.desktop.utils.RainbowIcons
import com.rainbow.desktop.utils.RainbowStrings
import com.rainbow.desktop.utils.defaultPadding
import com.rainbow.domain.models.PostCommentSorting

@Composable
fun LazyItemScope.CommentActions(
    sorting: PostCommentSorting,
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    setCommentSorting: (PostCommentSorting) -> Unit,
    onRefresh: () -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
) {
    Row(
        Modifier
            .clip(MaterialTheme.shapes.medium)
            .fillParentMaxWidth()
            .shadow(1.dp)
            .background(MaterialTheme.colorScheme.surface)
            .defaultPadding()
    ) {
        Row(Modifier.weight(1F), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RainbowIconButton(
                RainbowIcons.ArrowBack,
                RainbowStrings.NavigateBack,
                onBackClick,
                enabled = isBackEnabled,
            )

            RainbowIconButton(
                RainbowIcons.ArrowForward,
                RainbowStrings.NavigateForward,
                onForwardClick,
                enabled = isForwardEnabled
            )

            RainbowIconButton(
                RainbowIcons.Refresh,
                RainbowStrings.Refresh,
                onRefresh,
            )

            RainbowIconButton(
                RainbowIcons.UnfoldMore,
                RainbowStrings.ExpandComments,
                onExpand,
            )

            RainbowIconButton(
                RainbowIcons.UnfoldLess,
                RainbowStrings.CollapseComments,
                onCollapse,
            )
        }
        DropdownMenuHolder(
            sorting,
            setCommentSorting,
        )
    }
    Spacer(Modifier.height(16.dp))
}

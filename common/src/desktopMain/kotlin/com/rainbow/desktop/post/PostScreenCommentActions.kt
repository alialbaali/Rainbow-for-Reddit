package com.rainbow.desktop.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rainbow.common.components.RainbowIconButton
import com.rainbow.common.post.PostScreenModel
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.RainbowStrings
import com.rainbow.common.utils.defaultPadding
import com.rainbow.desktop.components.DropdownMenuHolder

@Composable
fun LazyItemScope.CommentActions(model: PostScreenModel) {
    val commentListModel by model.commentListModel.collectAsState()
    val backStack by model.backStack.collectAsState()
    val forwardStack by model.forwardStack.collectAsState()
    val sorting by commentListModel.sorting.collectAsState()
    Row(Modifier.fillParentMaxWidth().background(MaterialTheme.colors.surface).defaultPadding()) {
        CommentsActions(
            backStack.isNotEmpty(),
            forwardStack.isNotEmpty(),
            model::back,
            model::forward,
            commentListModel::refreshComments,
            commentListModel::expandComments,
            commentListModel::collapseComments,
            Modifier.weight(1F)
        )
        DropdownMenuHolder(
            sorting,
            commentListModel::setSorting,
        )
    }
    Spacer(Modifier.height(16.dp))
}


@Composable
private fun CommentsActions(
    isBackEnabled: Boolean,
    isForwardEnabled: Boolean,
    onBackClick: () -> Unit,
    onForwardClick: () -> Unit,
    onRefresh: () -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
}

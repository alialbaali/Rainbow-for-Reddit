package com.rainbow.desktop.post

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.domain.models.Post

@Composable
inline fun CompactPostItem(
    post: Post,
    noinline onUpdate: (Post) -> Unit,
    crossinline onClick: (Post) -> Unit,
    crossinline onUserNameClick: (String) -> Unit,
    crossinline onSubredditNameClick: (String) -> Unit,
    noinline onShowSnackbar: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
//    val isTextPost = remember(post) { post.type is Post.Type.Text }
//    val markPostAsRead by SettingsModel.markPostAsRead.collectAsState()
//
//    OneTimeEffect(markPostAsRead) {
//        if (markPostAsRead == MarkPostAsRead.OnScroll)
//            PostActionsModel.readPost(post, onUpdate)
//    }
//    Column(
//        modifier
//            .padding(vertical = 8.dp)
//            .defaultSurfaceShape()
//            .clickable {
//                onClick(post)
//                if (markPostAsRead == MarkPostAsRead.OnClick)
//                    PostActionsModel.readPost(post, onUpdate)
//            }
//            .defaultPadding(),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//    ) {
//        PostInfo(
//            post = post,
//            onUserNameClick,
//            onSubredditNameClick,
//            modifier = Modifier
//                .wrapContentWidth()
//                .wrapContentHeight()
//        )
//        if (isTextPost) {
//            PostTitle(
//                title = post.title,
//                isRead = post.isRead,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            )
//            PostContent(
//                post = post,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .wrapContentHeight()
//            )
//        } else {
//            Row(
//                Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//            ) {
//                PostTitle(
//                    title = post.title,
//                    isRead = post.isRead,
//                    modifier = Modifier
//                        .weight(1F)
//                )
//                PostContent(
//                    post = post,
//                    modifier = Modifier
//                        .height(100.dp)
//                )
//            }
//        }
//        PostActions(
//            post,
//            onClick,
//            onUpdate,
//            onShowSnackbar,
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight(),
//        )
//    }
}
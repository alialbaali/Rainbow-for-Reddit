package com.rainbow.app.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rainbow.app.components.BannerImage
import com.rainbow.app.components.ProfileImage
import com.rainbow.app.post.PostScreenType
import com.rainbow.app.post.Posts
import com.rainbow.app.utils.*
import com.rainbow.data.Repos
import com.rainbow.domain.models.User
import io.kamel.image.lazyImageResource

enum class ProfileTab {
    Overview, Comments, Submitted, Saved, Hidden, Upvoted, Downvoted;
}

@Composable
fun ProfileScreen() {

    val state by produceState<UIState<User>>(UIState.Loading) {
        Repos.User.getCurrentUser()
            .toUIState()
            .also { value = it }
    }


    state.composed { user ->

        val bannerImageResource = lazyImageResource(user.bannerImageUrl!!)

        val profileImageResource = lazyImageResource(user.imageUrl!!)

        val bannerImageHeight = 200.dp

        val profileImageSize = 200.dp

        val profileImageOffset = bannerImageHeight - profileImageSize / 2

        val bannerImageGradientHeight = bannerImageHeight.div(2.dp)

        val bannerImageGradient = Brush.verticalGradient(
            0F to Color.Transparent,
            1.0F to Color.Black,
            startY = bannerImageGradientHeight
        )

        var currentTab by remember { mutableStateOf(ProfileTab.Overview) }

        Row {

            Column(
                Modifier
                    .weight(1F)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(bannerImageHeight + profileImageSize / 2)
                ) {

                    Column(Modifier.fillMaxWidth()) {

                        BannerImage(
                            resource = bannerImageResource,
                            bannerColor = Color.Black.copy(0.1F),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(bannerImageHeight)
                                .drawWithContent {
                                    drawContent()
                                    drawRect(
                                        bannerImageGradient,
                                        topLeft = Offset(0F, bannerImageGradientHeight)
                                    )
                                }
                        )

//                        Surface(
//                            Modifier
//                                .fillMaxWidth()
//                                .height(100.dp),
//                        ) {
//
//                            Surface(
//                                Modifier
//                                    .fillMaxWidth()
//                                    .wrapContentHeight()
//                                    .height(100.dp)
////                                    .offset(profileImageOffset * 2)
//                                ,
//                            ) {
//
//
//                            }
//
//
//                        }

                    }

                    ProfileImage(
                        resource = profileImageResource,
                        primaryColor = Color.Black.copy(0.1F),
                        modifier = Modifier
                            .size(profileImageSize)
                            .defaultPadding()
                            .offset(y = profileImageOffset)
                    )

                    Text(
                        user.name,
                        fontSize = 36.sp,
                        color = MaterialTheme.colors.background,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .offset(x = profileImageOffset * 2, y = -profileImageOffset / 4)
                    )

                }

                ScrollableTabRow(currentTab.ordinal) {
                    ProfileTab.values().forEach { tab ->
                        Tab(
                            tab == currentTab,
                            { currentTab = tab },
                            text = {
                                Text(tab.name)
                            }
                        )
                    }
                }

                Posts(
                    PostScreenType.User("LoneWalker20"),
                    onSelectPost = {

                    }
                )

            }

//            PostScreen(Modifier.weight(1F))

        }

    }

}
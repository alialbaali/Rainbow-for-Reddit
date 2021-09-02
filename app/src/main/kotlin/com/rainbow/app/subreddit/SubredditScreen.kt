package com.rainbow.app.subreddit

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.BannerImage
import com.rainbow.app.components.ProfileImage
import com.rainbow.app.post.PostContent
import com.rainbow.app.post.PostListType
import com.rainbow.app.subreddit.SubredditViewModel.SubredditIntent
import com.rainbow.app.utils.*
import com.rainbow.domain.models.bannerColor
import com.rainbow.domain.models.primaryColor
import io.kamel.image.lazyPainterResource

@Composable
fun SubredditScreen(subredditName: String, onPostClick: (String) -> Unit, modifier: Modifier = Modifier) {

    val (state, emitIntent) = remember { SubredditViewModel() }

    var selectedTab by remember(subredditName) { mutableStateOf(SubredditTab.Default) }

    remember(subredditName) {
        emitIntent(SubredditIntent.GetSubreddit(subredditName))
    }

    state.subreddit.composed { subreddit ->

        val bannerImageResource = lazyPainterResource(subreddit.bannerImageUrl.toString())

        val profileImageResource = lazyPainterResource(subreddit.imageUrl.toString())

        val bannerImageHeight = 200.dp

        val profileImageSize = 200.dp

        val profileImageOffset = bannerImageHeight - profileImageSize / 2

        val bannerImageGradientHeight = bannerImageHeight.div(2.dp)

        val bannerImageGradient = Brush.verticalGradient(
            0F to Color.Transparent,
            1.0F to Color.Black,
            startY = bannerImageGradientHeight
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                BannerImage(
                    resource = bannerImageResource,
                    bannerColor = Color(subreddit.bannerColor),
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

                ProfileImage(
                    resource = profileImageResource,
                    primaryColor = Color(subreddit.primaryColor),
                    modifier = Modifier
                        .size(profileImageSize)
                        .defaultPadding()
                        .offset(y = profileImageOffset)
                )

                Text(
                    text = subreddit.title,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .defaultPadding()
                        .padding(start = profileImageSize),
                    style = MaterialTheme.typography.h3.copy(Color.White),
                    overflow = TextOverflow.Ellipsis,
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .defaultPadding()
                    .padding(start = profileImageSize),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Text(
                    text = subreddit.description,
                    modifier = Modifier
                        .fillMaxWidth(0.75F),
                    style = MaterialTheme.typography.h6,
                    overflow = TextOverflow.Ellipsis,
                )

                IconToggleButton(
                    checked = subreddit.isSubscribed,
                    onCheckedChange = {
                        if (it)
                            emitIntent(SubredditIntent.SubscribeSubreddit(subredditName))
                        else
                            emitIntent(SubredditIntent.UnSubscribeSubreddit(subredditName))
                    }
                ) {
                    Row {
                        if (subreddit.isSubscribed) {
                            Text("Subscribed")
                            Spacer(Modifier.width(8.dp))
                            Icon(RainbowIcons.Done, contentDescription = null)
                        } else {
                            Text("Subscribe")
                        }
                    }
                }

            }


            Spacer(Modifier.height(32.dp))

            SubredditTabRow(
                selectedTab,
                onTabClick = { selectedTab = it },
                Modifier
                    .fillMaxWidth()
            )

            when (selectedTab) {
                SubredditTab.Posts -> PostContent(
                    PostListType.Subreddit(subredditName),
                )
                SubredditTab.Rules -> SubredditRulesTab(subredditName)
                SubredditTab.Resources -> TODO()
                SubredditTab.RelatedSubreddits -> TODO()
                SubredditTab.Moderators -> TODO()


            }
        }

    }

}
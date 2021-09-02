package com.rainbow.app.subreddit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.rainbow.app.components.BannerImage
import com.rainbow.app.ui.dpDimensions
import com.rainbow.app.utils.defaultPadding
import com.rainbow.domain.models.Subreddit
import com.rainbow.domain.models.bannerColor
import io.kamel.image.lazyPainterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Subreddit(
    subreddit: Subreddit,
    onClick: (String) -> Unit,
    onSubscribe: (String) -> Unit,
    onUnSubscribe: (String) -> Unit,
    onFavorite: (String) -> Unit,
    onUnFavorite: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Surface(
        modifier
            .defaultPadding()
            .clickable { onClick(subreddit.id) },
        shape = MaterialTheme.shapes.large,
        elevation = MaterialTheme.dpDimensions.small,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dpDimensions.large)) {

            val imageResource = lazyPainterResource(subreddit.imageUrl.toString())

            BannerImage(
                imageResource,
                Color(subreddit.bannerColor),
                Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
            )

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {

                Text(
                    subreddit.name,
                    Modifier
                        .weight(1F),
                    style = MaterialTheme.typography.h6
                )

                IconToggleButton(
                    subreddit.isFavorite,
                    onCheckedChange = {
                        when (it) {
                            true -> onFavorite(subreddit.name)
                            false -> onUnFavorite(subreddit.name)
                        }
                    }
                ) {
                    val imageBitmap = when (subreddit.isFavorite) {
                        true -> imageResource("Icons/Star Icons/feather_star-full.png")
                        false -> imageResource("Icons/Star Icons/feather_star.png")
                    }

                    Icon(
                        imageBitmap,
                        null,
                        Modifier.size(24.dp),
                        when (subreddit.isFavorite) {
                            true -> MaterialTheme.colors.primaryVariant
                            false -> Color.Gray
                        }
                    )
                }

                TextButton(
                    onClick = {
                        if (subreddit.isSubscribed)
                            onUnSubscribe(subreddit.id)
                        else
                            onSubscribe(subreddit.id)
                    },
                ) {
                    Text("Subscribed")
                }

            }


        }
    }

}
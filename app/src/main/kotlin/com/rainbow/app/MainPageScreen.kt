package com.rainbow.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rainbow.app.post.PostList
import com.rainbow.app.post.PostType
import com.rainbow.domain.models.MainPage

@Composable
fun MainPageScreen(mainPage: MainPage, modifier: Modifier = Modifier) {
    PostList(
        PostType.Main(mainPage),
        {},
        {},
        modifier
    )
}

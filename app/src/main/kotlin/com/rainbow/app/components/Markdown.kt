package com.rainbow.app.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText

@Composable
fun MarkdownText(text: String, modifier: Modifier = Modifier) {
    RichText(modifier) {
        Markdown(text)
    }
}
package com.rainbow.common.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.rounded.FormatBold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Window
//import androidx.compose.ui.window.application
import com.halilibo.richtext.ui.RichText
import com.halilibo.richtext.ui.string.RichTextString
import com.halilibo.richtext.ui.string.Text
import com.rainbow.common.utils.RainbowIcons
import com.rainbow.common.utils.defaultPadding

//@Composable
//fun CreatePostWindow(onCloseRequest: () -> Unit, modifier: Modifier = Modifier) {
//    var text by remember { mutableStateOf("") }
//    val richTextBuilder = RichTextString.Builder()
//
//    var isBoldOn by remember { mutableStateOf(false) }
//
//    Window(onCloseRequest) {
//        RichText {
//            Text(richTextBuilder.toRichTextString())
//        }
//        TextField(
//            value = text,
//            onValueChange = {
//                text = it
//                richTextBuilder.append(it)
//            }
//        )
//        Row(Modifier.defaultPadding(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
//            IconButton(
//                onClick = {
//                    if (isBoldOn) {
//                        isBoldOn = false
//                        richTextBuilder.pop()
//                    } else {
//                        isBoldOn = true
//                        richTextBuilder.pushFormat(RichTextString.Format.Bold)
//                    }
//                }
//            ) {
//                Icon(RainbowIcons.FormatBold, "Bold")
//            }
//        }
//    }
//}
//
//fun main() = application {
//    CreatePostWindow(this::exitApplication)
//}
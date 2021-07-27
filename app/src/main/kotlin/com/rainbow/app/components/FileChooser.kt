package com.rainbow.app.components

import androidx.compose.desktop.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalWindowInfo
import java.awt.Dialog
import java.awt.FileDialog
import java.io.File
import javax.swing.JFileChooser

@Composable
inline fun FileChooser(
    title: String,
    onFilesSelection: (List<File>) -> Unit,
    initialDirectory: String? = null,
    multiSelectionEnabled: Boolean = false,
    acceptFiles: Map<File, String> = emptyMap(),
) {
    FileDialog(
        Dialog(
            ComposeWindow(),
            title
        )
    ).apply {
        isVisible = true
        isMultipleMode = multiSelectionEnabled
        directory = initialDirectory
        onFilesSelection(files.toList())
        acceptFiles.forEach {
            filenameFilter.accept(it.key, it.value)
        }

    }
}

fun main() = Window {

    JFileChooser()
        .apply {
            showDialog(ComposeWindow(), "Select")
        }

//    FileChooser("Select an image to upload", multiSelectionEnabled = true) {
//        it.onEach(::println)
//    }

}
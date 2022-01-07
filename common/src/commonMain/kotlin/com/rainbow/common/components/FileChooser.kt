package com.rainbow.common.components

import androidx.compose.runtime.Composable
//import androidx.compose.ui.awt.ComposeWindow
//import androidx.compose.ui.window.Window
//import androidx.compose.ui.window.application
//import java.awt.Dialog
//import java.awt.FileDialog
//import java.io.File
//import javax.swing.JFileChooser
//import javax.swing.UIManager

//
//@Composable
//inline fun FileChooser(
//    title: String,
//    onFilesSelection: (List<File>) -> Unit,
//    initialDirectory: String? = null,
//    multiSelectionEnabled: Boolean = false,
//    acceptFiles: Map<File, String> = emptyMap(),
//) {
//    FileDialog(
//        Dialog(
//            ComposeWindow(),
//            title
//        )
//    ).apply {
//        isVisible = true
//        isMultipleMode = multiSelectionEnabled
//        directory = initialDirectory
//        onFilesSelection(files.toList())
//        acceptFiles.forEach {
//            filenameFilter.accept(it.key, it.value)
//        }
//
//    }
//}
//
//fun main() = application {
//    Window({ exitApplication() }) {
//        JFileChooser()
//            .apply {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
//                showDialog(ComposeWindow(), "Select")
//            }
//
////    FileChooser("Select an image to upload", multiSelectionEnabled = true) {
////        it.onEach(::println)
////    }
//
//    }
//}
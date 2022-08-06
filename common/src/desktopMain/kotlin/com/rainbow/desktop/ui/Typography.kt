package com.rainbow.desktop.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

val rubikFonts = listOf(
    Font("Rubik/static/Rubik-Regular.ttf", FontWeight.Normal, FontStyle.Normal),
    Font("Rubik/static/Rubik-SemiBold.ttf", FontWeight.SemiBold, FontStyle.Normal),
    Font("Rubik/static/Rubik-SemiBoldItalic.ttf", FontWeight.SemiBold, FontStyle.Italic),
    Font("Rubik/static/Rubik-ExtraBold.ttf", FontWeight.ExtraBold, FontStyle.Normal),
    Font("Rubik/static/Rubik-ExtraBoldItalic.ttf", FontWeight.ExtraBold, FontStyle.Italic),
    Font("Rubik/static/Rubik-Black.ttf", FontWeight.Black, FontStyle.Normal),
    Font("Rubik/static/Rubik-BlackItalic.ttf", FontWeight.Black, FontStyle.Italic),
    Font("Rubik/static/Rubik-Light.ttf", FontWeight.Light, FontStyle.Normal),
    Font("Rubik/static/Rubik-LightItalic.ttf", FontWeight.Light, FontStyle.Italic),
    Font("Rubik/static/Rubik-Medium.ttf", FontWeight.Medium, FontStyle.Normal),
    Font("Rubik/static/Rubik-MediumItalic.ttf", FontWeight.Medium, FontStyle.Italic),
    Font("Rubik/static/Rubik-Bold.ttf", FontWeight.Bold, FontStyle.Normal),
    Font("Rubik/static/Rubik-BoldItalic.ttf", FontWeight.Bold, FontStyle.Italic),
)

val typography: Typography = Typography()
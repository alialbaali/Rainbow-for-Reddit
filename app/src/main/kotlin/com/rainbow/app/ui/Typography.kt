package com.rainbow.app.ui

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

private val rubikFonts = listOf(
    Font("fonts/Rubik/static/Rubik-Regular.ttf", FontWeight.Normal, FontStyle.Normal),
    Font("fonts/Rubik/static/Rubik-SemiBold.ttf", FontWeight.SemiBold, FontStyle.Normal),
    Font("fonts/Rubik/static/Rubik-SemiBoldItalic.ttf", FontWeight.SemiBold, FontStyle.Italic),
    Font("fonts/Rubik/static/Rubik-ExtraBold.ttf", FontWeight.ExtraBold, FontStyle.Normal),
    Font("fonts/Rubik/static/Rubik-ExtraBoldItalic.ttf", FontWeight.ExtraBold, FontStyle.Italic),
    Font("fonts/Rubik/static/Rubik-Black.ttf", FontWeight.Black, FontStyle.Normal),
    Font("fonts/Rubik/static/Rubik-BlackItalic.ttf", FontWeight.Black, FontStyle.Italic),
    Font("fonts/Rubik/static/Rubik-Light.ttf", FontWeight.Light, FontStyle.Normal),
    Font("fonts/Rubik/static/Rubik-LightItalic.ttf", FontWeight.Light, FontStyle.Italic),
    Font("fonts/Rubik/static/Rubik-Medium.ttf", FontWeight.Medium, FontStyle.Normal),
    Font("fonts/Rubik/static/Rubik-MediumItalic.ttf", FontWeight.Medium, FontStyle.Italic),
    Font("fonts/Rubik/static/Rubik-Bold.ttf", FontWeight.Bold, FontStyle.Normal),
    Font("fonts/Rubik/static/Rubik-BoldItalic.ttf", FontWeight.Bold, FontStyle.Italic),
)

val typography = Typography(defaultFontFamily = FontFamily(rubikFonts))
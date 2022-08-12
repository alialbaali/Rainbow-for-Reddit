package com.rainbow.desktop.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

val nunitoFonts = listOf(
    Font("font/Nunito-Regular.ttf", FontWeight.Normal, FontStyle.Normal),
    Font("font/Nunito-SemiBold.ttf", FontWeight.SemiBold, FontStyle.Normal),
    Font("font/Nunito-SemiBoldItalic.ttf", FontWeight.SemiBold, FontStyle.Italic),
    Font("font/Nunito-ExtraBold.ttf", FontWeight.ExtraBold, FontStyle.Normal),
    Font("font/Nunito-ExtraBoldItalic.ttf", FontWeight.ExtraBold, FontStyle.Italic),
    Font("font/Nunito-Black.ttf", FontWeight.Black, FontStyle.Normal),
    Font("font/Nunito-BlackItalic.ttf", FontWeight.Black, FontStyle.Italic),
    Font("font/Nunito-Light.ttf", FontWeight.Light, FontStyle.Normal),
    Font("font/Nunito-LightItalic.ttf", FontWeight.Light, FontStyle.Italic),
    Font("font/Nunito-Medium.ttf", FontWeight.Medium, FontStyle.Normal),
    Font("font/Nunito-MediumItalic.ttf", FontWeight.Medium, FontStyle.Italic),
    Font("font/Nunito-Bold.ttf", FontWeight.Bold, FontStyle.Normal),
    Font("font/Nunito-BoldItalic.ttf", FontWeight.Bold, FontStyle.Italic),
)

private val defaultTypography = Typography()

val typography: Typography = Typography(
    defaultTypography.displayLarge.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.displayMedium.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.displaySmall.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.headlineLarge.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.headlineMedium.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.headlineSmall.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.titleLarge.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.titleMedium.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.titleSmall.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.bodyLarge.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.bodyMedium.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.bodySmall.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.labelLarge.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.labelMedium.copy(fontFamily = FontFamily(nunitoFonts)),
    defaultTypography.labelSmall.copy(fontFamily = FontFamily(nunitoFonts)),
)
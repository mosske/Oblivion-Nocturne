package com.oblivion.nocturne.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp

/**
 * When true, [typo] keeps the original always-light text colors (pure white titles, #A8A8A8 body)
 * regardless of theme. Immersive screens drawn over dark artwork provide `true` so their text stays
 * readable at light theme. Everything else leaves it false and gets theme-aware colors.
 */
val LocalForceDarkText = staticCompositionLocalOf { false }

@Composable
fun typo(
    colorScheme: ColorScheme = MaterialTheme.colorScheme,
    forceDark: Boolean = LocalForceDarkText.current,
): Typography {
    // Oblivion mixes three families by role (see [FontFamilies]). Resolve them once per call.
    val display = displayFontFamily()
    val body = bodyFontFamily()
    val ui = uiFontFamily()

    // Titles were pure white, everything else muted gray (#A8A8A8) in the old dark-only palette.
    // forceDark keeps that; otherwise both come from the scheme (theme-aware light/dark).
    val titleColor = if (forceDark) Color.White else colorScheme.onBackground
    val bodyColor = if (forceDark) Color(0xFFA8A8A8) else colorScheme.onSurfaceVariant

    // Right-to-left layouts only. Compose gives every paragraph the layout's direction unless told
    // otherwise (TextStyle.resolveTextDirection), so a Latin title or URL was laid out right-to-left:
    // its closing punctuation jumped to the front, and titles mixing Latin with Hebrew came out
    // scrambled. Content lets each paragraph take its direction from its own text. Content alone
    // would also move Latin text to the left edge, so Right keeps it on the layout's start edge,
    // where a TextView keeps it with gravity START.
    val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val textDirection = if (rtl) TextDirection.Content else TextDirection.Unspecified
    val textAlign = if (rtl) TextAlign.Right else TextAlign.Unspecified

    fun base(font: FontFamily, color: Color): TextStyle =
        TextStyle(
            fontFamily = font,
            color = color,
            textDirection = textDirection,
            textAlign = textAlign,
        )

    val typo = Typography(
        /***
         * Display / hero roles — Cinzel. Big names (artist, album) and section headers in Home, Mood,
         * Genre, Playlist etc.
         */
        titleSmall = base(display, titleColor).copy(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
        titleMedium = base(display, titleColor).copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
        titleLarge = base(display, titleColor).copy(fontSize = 25.sp, fontWeight = FontWeight.Bold),
        bodySmall = base(body, bodyColor).copy(fontSize = 11.sp, fontWeight = FontWeight.Normal),
        bodyMedium = base(body, bodyColor).copy(fontSize = 13.sp, fontWeight = FontWeight.Normal),
        bodyLarge = base(body, bodyColor).copy(fontSize = 18.sp, fontWeight = FontWeight.Normal),
        displayLarge = base(body, bodyColor).copy(fontSize = 20.sp, fontWeight = FontWeight.Normal),
        headlineMedium = base(display, bodyColor).copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        headlineLarge = base(display, bodyColor).copy(fontSize = 23.sp, fontWeight = FontWeight.Bold),
        labelMedium = base(ui, bodyColor).copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
        labelSmall = base(ui, bodyColor).copy(fontSize = 14.sp, fontWeight = FontWeight.Bold),
    )
    return typo
}

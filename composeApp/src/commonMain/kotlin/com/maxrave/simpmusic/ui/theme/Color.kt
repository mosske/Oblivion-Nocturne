package com.oblivion.nocturne.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ===== Brand =====

/**
 * Brand seed color: the gold of the Oblivion-Nocturne logo (#D4AF37). The whole Material 3
 * ColorScheme is generated from this at runtime when dynamic colour is on — see [AppTheme]. With
 * dynamic colour disabled (see Theme.kt) the app instead uses the fixed [brandDarkColorScheme] /
 * [brandLightColorScheme] built directly from the logo hex values below.
 * */
val seed = Color(0xFFD4AF37)

/**
 * Fixed Material 3 dark palette, built straight from the Oblivion-Nocturne logo:
 *   - primary (gold):      #D4AF37
 *   - accent / secondary:  ethereal blue #1E3A5F
 *   - background:          charcoal #121212
 *   - surface:             #1E1E1E
 *   - on-colours:          off-white (#FAFAFA)
 * Used by [AppTheme] once dynamic colour is disabled. Neutrals follow the M3 tonal ramp so the
 * container tiers still read as a coherent hierarchy above the charcoal base; error stays on the
 * app's favorite pink for consistency with the semantic colors below.
 * */
val brandDarkColorScheme = darkColorScheme(
    primary = Color(0xFFD4AF37),
    onPrimary = Color(0xFF2E2915),

    secondary = Color(0xFF1E3A5F),
    onSecondary = Color(0xFFF6F9FC),

    tertiary = Color(0xFF6E5B74),
    onTertiary = Color(0xFFF6F9FC),

    onError = Color(0xFFFF4081),
    errorContainer = Color(0xFF93122D),
    onErrorContainer = Color(0xFF3A081F),

    background = Color(0xFF121212),
    onBackground = Color(0xFFFAFAFA),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFFAFAFA),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFB3B3B3),

    surfaceContainerLowest = Color(0xFF141414),
    surfaceContainerLow = Color(0xFF171717),
    surfaceContainer = Color(0xFF1C1C1C),
    surfaceContainerHigh = Color(0xFF212121),
    surfaceContainerHighest = Color(0xFF262626),

    outline = Color(0xFF3A3A3A),
    outlineVariant = Color(0xFF414141),

    inverseSurface = Color(0xFFFAFAFA),
    inverseOnSurface = Color(0xFF121212),
    inversePrimary = Color(0xFFD4AF37),
    inversePrimary = Color(0xFF2E2915),

    // Standard Material 3 parameters for M3 1.5.0-alpha22
    onPrimaryFixed = Color(0xFF2E2915),
    onPrimaryFixedVariant = Color(0xFFF6F9FC),

    secondaryFixed = Color(0xFF1E3A5F),
    secondaryFixedDim = Color(0xFF28476B),
    onSecondaryFixed = Color(0xFFF6F9FC),
    onSecondaryFixedVariant = Color(0xFF1E3A5F),

    tertiaryFixed = Color(0xFF6E5B74),
    tertiaryFixedDim = Color(0xFF28202C),
    onTertiaryFixed = Color(0xFFF6F9FC),
    onTertiaryFixedVariant = Color(0xFF6E5B74),

    surfaceBright = Color(0xFF2A2A2A),
    surfaceDim = Color(0xFF141414),
)

/**
 * Fixed Material 3 light palette, mirroring the dark one above so dynamic colour being off still
 * renders a coherent brand look. Surfaces follow the app's existing neutral-grey ramp (#FAFAFA page,
 * #F7F7F7 surfaces); primary/accent stay gold + ethereal blue; on-colours are charcoal.
 * */
val brandLightColorScheme = lightColorScheme(
    primary = Color(0xFFD4AF37),
    onPrimary = Color(0xFFFDFCF9),

    secondary = Color(0xFF1E3A5F),
    onSecondary = Color(0xFFF6F9FC),

    tertiary = Color(0xFF6E5B74),
    onTertiary = Color(0xFFF6F9FC),

    onError = Color(0xFFFF4081),
    errorContainer = Color(0xFFFFD8E2),
    onErrorContainer = Color(0xFFFF9EAF),

    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF121212),
    surface = Color(0xFFF7F7F7),
    onSurface = Color(0xFF1B1B1B),
    surfaceVariant = Color(0xFFE2E2E2),
    onSurfaceVariant = Color(0xFF474747),

    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF7F7F7),
    surfaceContainer = Color(0xFFF1F1F1),
    surfaceContainerHigh = Color(0xFFECECEC),
    surfaceContainerHighest = Color(0xFFE6E6E6),

    outline = Color(0xFF777777),
    outlineVariant = Color(0xFFC7C7C7),

    inverseSurface = Color(0xFF303030),
    inverseOnSurface = Color(0xFFF1F1F1),
    inversePrimary = Color(0xFFD4AF37),
    inversePrimary = Color(0xFF2E2915),

    // Standard Material 3 parameters for M3 1.5.0-alpha22
    onPrimaryFixed = Color(0xFFFDFCF9),
    onPrimaryFixedVariant = Color(0xFF3A352C),

    secondaryFixed = Color(0xFF1E3A5F),
    secondaryFixedDim = Color(0xFF162B45),
    onSecondaryFixed = Color(0xFFF6F9FC),
    onSecondaryFixedVariant = Color(0xFF1E3A5F),

    tertiaryFixed = Color(0xFF6E5B74),
    tertiaryFixedDim = Color(0xFF4D4152),
    onTertiaryFixed = Color(0xFFF6F9FC),
    onTertiaryFixedVariant = Color(0xFF6E5B74),

    surfaceBright = Color(0xFFFFFFFF),
    surfaceDim = Color(0xFFEAEAEA),
)

// ===== Semantic colors (not derivable from the color scheme) =====

/** Liked/favorite state (heart buttons, favorite tiles). */
val favoriteColor = Color(0xFFFF4081)

/** Currently playing lyric line. */
val lyricActiveColor = Color(0xFFFFFF00)

val shimmerBackground = Color(0x7E383737)
val shimmerLine = Color(0xFF4D4848)

// Light-theme counterparts of the shimmer tokens.
val shimmerBackgroundLight = Color(0x7EDCD8D8)
val shimmerLineLight = Color(0xFFCFC8C8)

val overlay = Color(0x32242424)
val blackMoreOverlay = Color(0x8f242424)

// ===== Desktop shell =====

val desktopWindowDark = Color(0xFF000000)
val desktopWindowLight = Color(0xFFFFFFFF)
val desktopPanelDark = Color(0xFF121212)

// ===== Desktop window controls =====

val windowCloseButton = Color(0xFFFF605C)
val windowCloseButtonHover = Color(0xFFE54942)
val windowMinimiseButton = Color(0xFFFFBD44)
val windowMinimiseButtonHover = Color(0xFFE5A93D)
val windowMaximiseButton = Color(0xFF00CA4E)
val windowMaximiseButtonHover = Color(0xFF00B344)

// ===== Legacy — do not add new usages =====

@Deprecated("Legacy storage bar color only — use MaterialTheme.colorScheme.primary in new code")
val md_theme_dark_primary = Color(0xFFB2C5FF)

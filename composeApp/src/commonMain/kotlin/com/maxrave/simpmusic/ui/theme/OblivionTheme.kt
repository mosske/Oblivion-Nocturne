package com.oblivion.nocturne.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Oblivion Music design tokens, extracted from styles/themes.css (the default "Candyland" theme).
 *
 * The whole app is a deep emerald canvas with warm gold accents:
 *   - Canvas / neutrals : #051911 -> #1f1b22 -> #131116 (radial gradient)
 *   - Accent             : #dfb86c, gradient #f7e2ab -> #dfb86c -> #a07422
 *   - Text               : near-white #fdfcf9, muted #b0a694
 *
 * [oblivionDarkColorScheme] takes the device-adaptive Material 3 scheme produced by
 * rememberDynamicColorScheme and overrides only the visible accent + canvas fields with these
 * exact values. Everything else (error, inverse*, outline) is inherited from the adaptive base so
 * the palette stays coherent while keeping Android's dynamic-island behaviour for the parts that
 * don't matter visually.
 */

@Immutable
object OblivionPalette {
    // --- Canvas / neutrals (radial gradient endpoints) ---
    val canvasDeep = Color(0xFF051911)
    val canvasMid = Color(0xFF1F1B22)
    val canvasBase = Color(0xFF131116)

    // --- Accent gold + gradient stops ---
    val accentGold = Color(0xFFDFB86C)
    val accentLight = Color(0xFFF7E2AB)
    val accentDark = Color(0xFFA07422)

    // --- Text ---
    val textWhite = Color(0xFFFDFCF9)
    val textMuted = Color(0xFFB0A694)

    /** Dark warm brown used for text drawn *on* gold (primary/secondary/tertiary). */
    val onGoldText = Color(0xFF3A2F1C)
}

/**
 * Overrides the accent trio + canvas neutrals of [base] with Oblivion's exact palette.
 *
 * @param base the device-adaptive scheme from rememberDynamicColorScheme (kept for error/outline).
 */
fun oblivionDarkColorScheme(base: ColorScheme): ColorScheme = base.copy(
    // Accent — warm gold everywhere, with tonal variation so the three roles aren't flat.
    primary = OblivionPalette.accentGold,
    onPrimary = OblivionPalette.onGoldText,
    primaryContainer = Color(0xFF4D4130),
    onPrimaryContainer = OblivionPalette.accentLight,

    secondary = OblivionPalette.accentGold.copy(alpha = 0.8f),
    onSecondary = OblivionPalette.onGoldText,
    secondaryContainer = Color(0xFF3A343B),
    onSecondaryContainer = Color(0xFFE7DCCE),

    tertiary = OblivionPalette.accentDark,
    onTertiary = OblivionPalette.onGoldText,
    tertiaryContainer = Color(0xFF4D3E26),
    onTertiaryContainer = OblivionPalette.accentLight,

    // Canvas — deep emerald, layered with a radial gradient by custom components.
    background = OblivionPalette.canvasDeep,
    onBackground = OblivionPalette.textWhite,
    surface = OblivionPalette.canvasBase,
    onSurface = OblivionPalette.textWhite,
    surfaceVariant = Color(0xFF2A262D), // translucent glass card base (rgba(42,38,45,.72))
    onSurfaceVariant = OblivionPalette.textMuted,

    surfaceTint = OblivionPalette.accentGold,
)

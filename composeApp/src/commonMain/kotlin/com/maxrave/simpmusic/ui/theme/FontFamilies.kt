package com.oblivion.nocturne.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import oblivionnocturne.composeapp.generated.resources.Res
import oblivionnocturne.composeapp.generated.resources.cinzel_variable
import oblivionnocturne.composeapp.generated.resources.cormorant_garamond_italic
import oblivionnocturne.composeapp.generated.resources.cormorant_garamond_regular
import oblivionnocturne.composeapp.generated.resources.montserrat_variable

/**
 * Oblivion Music type roles. The old app used a single Poppins family everywhere; Oblivion instead
 * mixes three Google Fonts:
 *   - display  : Cinzel (Roman capitals) — hero titles, artist/album names, section headers
 *   - body     : Cormorant Garamond — all running text and captions
 *   - ui       : Montserrat — buttons, labels, pill chips, numbers
 *
 * Each accessor returns a [FontFamily] so callers can pass it straight into TextStyle. The family
 * accessors are @Composable because Compose only accepts a FontFamily built from resources inside a
 * composition (it needs the resource handle resolved at render time).
 */

/** Cinzel — display / hero headings. ExtraBold for big names, SemiBold for small captions. */
@Composable
fun displayFontFamily(): FontFamily = FontFamily(
    Font(Res.font.cinzel_variable, FontWeight.ExtraBold),
)

/** Cormorant Garamond — body text. Registers both the regular and italic faces so
 * [FontStyle.Italic] resolves to the true italic cut rather than a slanted regular glyph. */
@Composable
fun bodyFontFamily(): FontFamily = FontFamily(
    Font(Res.font.cormorant_garamond_regular, FontWeight.Normal),
    Font(Res.font.cormorant_garamond_italic, FontWeight.Normal, style = FontStyle.Italic),
)

/** Montserrat — UI chrome: buttons, labels, chips and numeric readouts. */
@Composable
fun uiFontFamily(): FontFamily = FontFamily(
    Font(Res.font.montserrat_variable, FontWeight.SemiBold),
)

/** Single-family accessor used by upstream call sites that need one family (e.g. the text link in App). */
@Composable
fun fontFamily(): FontFamily = uiFontFamily()

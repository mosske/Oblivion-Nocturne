package org.oblivion.nocturne.lyrics.models.response

import kotlinx.serialization.Serializable

@Serializable
data class BetterLyricsResponse(
    val ttml: String,
)
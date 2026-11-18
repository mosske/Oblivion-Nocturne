package com.oblivion.nocturne.domain.data.model.metadata

import kotlinx.serialization.Serializable

@Serializable
data class Lyrics(
    val error: Boolean = false,
    val lines: List<Line>?,
    val syncType: String?,
    val oblivionNocturneLyrics: OblivionNocturneLyrics? = null,
)

@Serializable
data class OblivionNocturneLyrics(
    val id: String,
    val vote: Int,
)
package com.oblivion.nocturne.kotlinytmusicscraper.models.youtube

import com.oblivion.nocturne.kotlinytmusicscraper.models.Thumbnail
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Thumbnails(
    @SerialName("thumbnails")
    val thumbnails: List<Thumbnail>? = null,
)
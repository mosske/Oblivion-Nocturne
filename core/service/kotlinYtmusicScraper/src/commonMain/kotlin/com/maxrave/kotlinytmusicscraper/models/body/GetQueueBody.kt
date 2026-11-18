package com.oblivion.nocturne.kotlinytmusicscraper.models.body

import com.oblivion.nocturne.kotlinytmusicscraper.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetQueueBody(
    val context: Context,
    val videoIds: List<String>?,
    val playlistId: String?,
)
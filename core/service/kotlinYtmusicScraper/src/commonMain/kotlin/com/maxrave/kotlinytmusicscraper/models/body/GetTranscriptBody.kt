package com.oblivion.nocturne.kotlinytmusicscraper.models.body

import com.oblivion.nocturne.kotlinytmusicscraper.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetTranscriptBody(
    val context: Context,
    val params: String,
)
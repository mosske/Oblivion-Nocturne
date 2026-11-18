package com.oblivion.nocturne.kotlinytmusicscraper.models.body

import com.oblivion.nocturne.kotlinytmusicscraper.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetSearchSuggestionsBody(
    val context: Context,
    val input: String,
)
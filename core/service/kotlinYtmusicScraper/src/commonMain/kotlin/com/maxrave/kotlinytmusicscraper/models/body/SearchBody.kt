package com.oblivion.nocturne.kotlinytmusicscraper.models.body

import com.oblivion.nocturne.kotlinytmusicscraper.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class SearchBody(
    val context: Context,
    val query: String?,
    val params: String?,
)
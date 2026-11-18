package com.oblivion.nocturne.kotlinytmusicscraper.pages

import com.oblivion.nocturne.kotlinytmusicscraper.models.SongItem

data class PlaylistContinuationPage(
    val songs: List<SongItem>,
    val continuation: String?,
)
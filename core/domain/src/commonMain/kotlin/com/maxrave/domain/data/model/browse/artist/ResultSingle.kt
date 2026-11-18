package com.oblivion.nocturne.domain.data.model.browse.artist

import com.oblivion.nocturne.domain.data.model.searchResult.songs.Thumbnail
import com.oblivion.nocturne.domain.data.type.HomeContentType

data class ResultSingle(
    val browseId: String,
    val thumbnails: List<Thumbnail>,
    val title: String,
    val year: String,
) : HomeContentType
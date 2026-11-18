package com.oblivion.nocturne.domain.data.model.mood.genre

import com.oblivion.nocturne.domain.data.model.searchResult.songs.Thumbnail
import com.oblivion.nocturne.domain.data.type.HomeContentType

data class Content(
    val playlistBrowseId: String,
    val thumbnail: List<Thumbnail>?,
    val title: Title,
) : HomeContentType
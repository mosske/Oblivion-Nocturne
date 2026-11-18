package com.oblivion.nocturne.domain.data.model.home.chart

import com.oblivion.nocturne.domain.data.model.browse.artist.ResultPlaylist

data class ChartItemPlaylist(
    val title: String,
    val playlists: List<ResultPlaylist>,
)
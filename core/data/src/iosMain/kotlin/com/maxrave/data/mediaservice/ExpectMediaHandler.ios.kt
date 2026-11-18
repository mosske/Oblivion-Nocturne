package com.oblivion.nocturne.data.mediaservice

import com.oblivion.nocturne.domain.repository.DiscordRepository

actual fun createMediaServiceHandler(
    dataStoreManager: com.oblivion.nocturne.domain.manager.DataStoreManager,
    songRepository: com.oblivion.nocturne.domain.repository.SongRepository,
    streamRepository: com.oblivion.nocturne.domain.repository.StreamRepository,
    localPlaylistRepository: com.oblivion.nocturne.domain.repository.LocalPlaylistRepository,
    analyticsRepository: com.oblivion.nocturne.domain.repository.AnalyticsRepository,
    discordRepository: DiscordRepository,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
): com.oblivion.nocturne.domain.mediaservice.handler.MediaPlayerHandler {
    TODO("Not yet implemented")
}
package com.oblivion.nocturne.data.mediaservice

import com.oblivion.nocturne.domain.manager.DataStoreManager
import com.oblivion.nocturne.domain.mediaservice.handler.MediaPlayerHandler
import com.oblivion.nocturne.domain.repository.AnalyticsRepository
import com.oblivion.nocturne.domain.repository.DiscordRepository
import com.oblivion.nocturne.domain.repository.LocalPlaylistRepository
import com.oblivion.nocturne.domain.repository.SongRepository
import com.oblivion.nocturne.domain.repository.StreamRepository
import kotlinx.coroutines.CoroutineScope

expect fun createMediaServiceHandler(
    dataStoreManager: DataStoreManager,
    songRepository: SongRepository,
    streamRepository: StreamRepository,
    localPlaylistRepository: LocalPlaylistRepository,
    analyticsRepository: AnalyticsRepository,
    discordRepository: DiscordRepository,
    coroutineScope: CoroutineScope,
): MediaPlayerHandler
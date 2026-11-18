package com.oblivion.nocturne.data.di

import com.oblivion.nocturne.common.Config
import com.oblivion.nocturne.data.mediaservice.createMediaServiceHandler
import com.oblivion.nocturne.domain.mediaservice.handler.MediaPlayerHandler
import kotlinx.coroutines.CoroutineScope
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mediaHandlerModule =
    module {
        single<MediaPlayerHandler>(createdAtStart = true) {
            createMediaServiceHandler(
                dataStoreManager = get(),
                songRepository = get(),
                streamRepository = get(),
                localPlaylistRepository = get(),
                analyticsRepository = get(),
                discordRepository = get(),
                coroutineScope = get<CoroutineScope>(named(Config.SERVICE_SCOPE)),
            )
        }
    }
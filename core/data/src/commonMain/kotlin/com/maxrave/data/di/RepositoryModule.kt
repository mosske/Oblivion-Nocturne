package com.oblivion.nocturne.data.di

import com.oblivion.nocturne.common.AppIdentity
import com.oblivion.nocturne.common.Config.SERVICE_SCOPE
import com.oblivion.nocturne.data.io.fileDir
import com.oblivion.nocturne.data.repository.AccountRepositoryImpl
import com.oblivion.nocturne.data.repository.AlbumRepositoryImpl
import com.oblivion.nocturne.data.repository.AnalyticsRepositoryImpl
import com.oblivion.nocturne.data.repository.ArtistRepositoryImpl
import com.oblivion.nocturne.data.repository.AutoEqRepositoryImpl
import com.oblivion.nocturne.data.lyrics.LyricsRomanizerRepositoryImpl
import com.oblivion.nocturne.data.repository.CommonRepositoryImpl
import com.oblivion.nocturne.data.repository.DiscordRepositoryImpl
import com.oblivion.nocturne.data.repository.HomeRepositoryImpl
import com.oblivion.nocturne.data.repository.ImportRepositoryImpl
import com.oblivion.nocturne.data.repository.LocalPlaylistRepositoryImpl
import com.oblivion.nocturne.data.repository.LyricsCanvasRepositoryImpl
import com.oblivion.nocturne.data.repository.PlaylistRepositoryImpl
import com.oblivion.nocturne.data.repository.PodcastRepositoryImpl
import com.oblivion.nocturne.data.repository.SearchRepositoryImpl
import com.oblivion.nocturne.data.repository.SongRepositoryImpl
import com.oblivion.nocturne.data.repository.StreamRepositoryImpl
import com.oblivion.nocturne.data.repository.TasteRepositoryImpl
import com.oblivion.nocturne.data.repository.UpdateRepositoryImpl
import com.oblivion.nocturne.domain.repository.AccountRepository
import com.oblivion.nocturne.domain.repository.AlbumRepository
import com.oblivion.nocturne.domain.repository.AnalyticsRepository
import com.oblivion.nocturne.domain.repository.ArtistRepository
import com.oblivion.nocturne.domain.repository.AutoEqRepository
import com.oblivion.nocturne.domain.repository.LyricsRomanizerRepository
import com.oblivion.nocturne.domain.repository.CommonRepository
import com.oblivion.nocturne.domain.repository.DiscordRepository
import com.oblivion.nocturne.domain.repository.HomeRepository
import com.oblivion.nocturne.domain.repository.ImportRepository
import com.oblivion.nocturne.domain.repository.LocalPlaylistRepository
import com.oblivion.nocturne.domain.repository.LyricsCanvasRepository
import com.oblivion.nocturne.domain.repository.PlaylistRepository
import com.oblivion.nocturne.domain.repository.PodcastRepository
import com.oblivion.nocturne.domain.repository.SearchRepository
import com.oblivion.nocturne.domain.repository.SongRepository
import com.oblivion.nocturne.domain.repository.StreamRepository
import com.oblivion.nocturne.domain.repository.TasteRepository
import com.oblivion.nocturne.domain.repository.UpdateRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule =
    module {
        single<AccountRepository>(createdAtStart = true) {
            AccountRepositoryImpl(get(), get())
        }

        single<AlbumRepository>(createdAtStart = true) {
            AlbumRepositoryImpl(get(), get())
        }

        single<ArtistRepository>(createdAtStart = true) {
            ArtistRepositoryImpl(get(), get(), get())
        }

        single<CommonRepository>(createdAtStart = true) {
            CommonRepositoryImpl(get(named(SERVICE_SCOPE)), get(), get(), get(), get(), get()).apply {
                this.init("${fileDir()}/ytdlp-cookie.txt", get(), get<AppIdentity>().isDevBuild)
            }
        }

        // At start: a Full build drops the token kizzy's login left behind as soon as it launches.
        single<DiscordRepository>(createdAtStart = true) {
            DiscordRepositoryImpl(get())
        }

        // Lazy for the same reason its client is: the picker is the only thing that wants it.
        single<AutoEqRepository> {
            AutoEqRepositoryImpl(get(), get())
        }

        // Lazy: constructing it costs a few File.length() calls (plus, while the pack is missing,
        // one background read of the stored selection), but the kuromoji dictionary behind it is
        // loaded on first Japanese line, or right after a download installs it, and never at
        // startup — so this must NOT be createdAtStart, or every launch pays for a feature most
        // listeners leave off. The path is where Android keeps the downloaded ipadic pack (the
        // APK no longer bundles it); Desktop and iOS ignore it.
        single<LyricsRomanizerRepository> {
            LyricsRomanizerRepositoryImpl("${fileDir()}/kuromoji-ipadic", get())
        }

        single<HomeRepository>(createdAtStart = true) {
            HomeRepositoryImpl(get(), get())
        }

        single<ImportRepository>(createdAtStart = true) {
            ImportRepositoryImpl(get())
        }

        single<LocalPlaylistRepository>(createdAtStart = true) {
            LocalPlaylistRepositoryImpl(get(), get())
        }

        single<LyricsCanvasRepository>(createdAtStart = true) {
            LyricsCanvasRepositoryImpl(get(), get(), get(), get(), get())
        }

        single<PlaylistRepository>(createdAtStart = true) {
            PlaylistRepositoryImpl(get(), get(), get())
        }

        single<PodcastRepository>(createdAtStart = true) {
            PodcastRepositoryImpl(get(), get())
        }

        single<SearchRepository>(createdAtStart = true) {
            SearchRepositoryImpl(get(), get())
        }

        single<SongRepository>(createdAtStart = true) {
            SongRepositoryImpl(get(), get(), get())
        }

        single<StreamRepository>(createdAtStart = true) {
            StreamRepositoryImpl(get(), get())
        }

        single<UpdateRepository>(createdAtStart = true) {
            UpdateRepositoryImpl()
        }

        single<AnalyticsRepository>(createdAtStart = true) {
            AnalyticsRepositoryImpl(get())
        }

        // Lazy: only the taste card in Library asks for it.
        single<TasteRepository> {
            TasteRepositoryImpl(get(), get())
        }
    }
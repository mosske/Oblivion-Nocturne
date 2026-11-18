package com.oblivion.nocturne.di

import com.oblivion.nocturne.viewModel.AlbumViewModel
import com.oblivion.nocturne.utils.VersionManager
import com.oblivion.nocturne.viewModel.AnalyticsViewModel
import com.oblivion.nocturne.viewModel.ListenTogetherSettingsViewModel
import com.oblivion.nocturne.viewModel.ListenTogetherViewModel
import com.oblivion.nocturne.viewModel.ArtistViewModel
import com.oblivion.nocturne.viewModel.BrowseViewModel
import com.oblivion.nocturne.viewModel.BrowserLoginViewModel
import com.oblivion.nocturne.viewModel.CommentsViewModel
import com.oblivion.nocturne.viewModel.SoundCloudPlayerViewModel
import com.oblivion.nocturne.viewModel.HomeViewModel
import com.oblivion.nocturne.viewModel.ImportViewModel
import com.oblivion.nocturne.viewModel.LibraryDynamicPlaylistViewModel
import com.oblivion.nocturne.viewModel.LibrarySidebarViewModel
import com.oblivion.nocturne.viewModel.LibraryViewModel
import com.oblivion.nocturne.viewModel.LocalPlaylistViewModel
import com.oblivion.nocturne.viewModel.LogInViewModel
import com.oblivion.nocturne.viewModel.LoginSyncHostViewModel
import com.oblivion.nocturne.viewModel.LoginSyncSenderViewModel
import com.oblivion.nocturne.viewModel.MoodViewModel
import com.oblivion.nocturne.viewModel.MoreAlbumsViewModel
import com.oblivion.nocturne.viewModel.NotificationViewModel
import com.oblivion.nocturne.viewModel.NowPlayingBottomSheetViewModel
import com.oblivion.nocturne.viewModel.PlaylistViewModel
import com.oblivion.nocturne.viewModel.PodcastViewModel
import com.oblivion.nocturne.viewModel.SearchViewModel
import com.oblivion.nocturne.viewModel.AutoEqViewModel
import com.oblivion.nocturne.viewModel.SettingsViewModel
import com.oblivion.nocturne.viewModel.SharedViewModel
import com.oblivion.nocturne.viewModel.SongSelectionViewModel
import com.oblivion.nocturne.viewModel.TasteViewModel
import com.oblivion.nocturne.viewModel.WrappedViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        single {
            SharedViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        single {
            SearchViewModel(
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            SongSelectionViewModel(
                get(),
                get(),
            )
        }
        // Only one of these resolves on a given platform: Desktop binds the host repository, Android the
        // sender. Koin resolves dependencies when a ViewModel is created, not when it is declared.
        viewModel { LoginSyncHostViewModel(get()) }
        viewModel { LoginSyncSenderViewModel(get()) }
        viewModel { BrowserLoginViewModel(get()) }
        viewModel { CommentsViewModel(get(), get()) }
        viewModel { SoundCloudPlayerViewModel(get()) }
        viewModel {
            NowPlayingBottomSheetViewModel(
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LibraryViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LibraryDynamicPlaylistViewModel(
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            ImportViewModel(
                get(),
            )
        }
        viewModel {
            AlbumViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            HomeViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            AutoEqViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            SettingsViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            ArtistViewModel(
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            PlaylistViewModel(
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LogInViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            PodcastViewModel(
                get(),
            )
        }
        viewModel {
            MoreAlbumsViewModel(
                get(),
            )
        }
        viewModel {
            BrowseViewModel(
                get(),
            )
        }
        viewModel {
            LibrarySidebarViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            LocalPlaylistViewModel(
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            NotificationViewModel(
                get(),
            )
        }
        viewModel {
            MoodViewModel(
                get(),
                get(),
            )
        }
        viewModel {
            AnalyticsViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            WrappedViewModel(
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            TasteViewModel(
                get(),
                get(),
                get(),
                get(),
                get(),
                get(),
            )
        }
        viewModel {
            ListenTogetherSettingsViewModel(get())
        }
        viewModel {
            ListenTogetherViewModel(
                repository = get(),
                dataStore = get(),
                bridge = get(),
            )
        }

    }

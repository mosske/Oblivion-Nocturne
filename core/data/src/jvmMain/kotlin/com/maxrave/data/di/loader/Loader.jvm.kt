package com.oblivion.nocturne.data.di.loader

import com.oblivion.nocturne.media_jvm.di.loadDesktopPlayerModule
import com.oblivion.nocturne.data.browserlogin.BrowserLoginRepositoryImpl
import com.oblivion.nocturne.data.librarysync.LibrarySyncHostRepositoryImpl
import com.oblivion.nocturne.data.librarysync.SyncApplier
import com.oblivion.nocturne.data.librarysync.SyncEngine
import com.oblivion.nocturne.data.librarysync.SyncedSettings
import com.oblivion.nocturne.data.loginsync.LoginSyncHostRepositoryImpl
import com.oblivion.nocturne.data.loginsync.LoginSyncStore
import com.oblivion.nocturne.domain.repository.LibrarySyncHostRepository
import com.oblivion.nocturne.domain.repository.LibrarySyncRepository
import com.oblivion.nocturne.domain.repository.LoginSyncHostRepository
import com.oblivion.nocturne.domain.repository.BrowserLoginRepository
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

actual fun loadMediaService() {
    loadDesktopPlayerModule()
}

actual fun loadLoginSyncModule() {
    loadKoinModules(
        module {
            single { LoginSyncStore(get(), get(), get()) }
            single<LoginSyncHostRepository> { LoginSyncHostRepositoryImpl(get()) }
            // Library sync (#2663): this computer keeps the log and serves its paired phones.
            single { SyncedSettings(get(), get()) }
            single { SyncEngine(get(), get(), SyncApplier(get(), get()), get()) }
            single<LibrarySyncHostRepository> { LibrarySyncHostRepositoryImpl(get(), get(), get(), get(), get(), get()) }
            single<LibrarySyncRepository> { get<LibrarySyncHostRepository>() }
            single<BrowserLoginRepository> { BrowserLoginRepositoryImpl() }
        },
    )
}

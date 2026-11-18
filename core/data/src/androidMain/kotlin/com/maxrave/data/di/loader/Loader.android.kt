package com.oblivion.nocturne.data.di.loader

import com.oblivion.nocturne.media3.di.loadMediaService
import com.oblivion.nocturne.data.librarysync.LibrarySyncPhoneRepositoryImpl
import com.oblivion.nocturne.data.librarysync.SyncApplier
import com.oblivion.nocturne.data.librarysync.SyncEngine
import com.oblivion.nocturne.data.librarysync.SyncedSettings
import com.oblivion.nocturne.data.loginsync.LoginSyncSenderRepositoryImpl
import com.oblivion.nocturne.data.loginsync.LoginSyncStore
import com.oblivion.nocturne.domain.repository.LibrarySyncPhoneRepository
import com.oblivion.nocturne.domain.repository.LibrarySyncRepository
import com.oblivion.nocturne.domain.repository.LoginSyncSenderRepository
import org.koin.core.context.loadKoinModules
import org.oblivion.nocturne.loginsync.LoginSyncClient
import org.oblivion.nocturne.loginsync.PairedSyncClient
import org.koin.dsl.module

actual fun loadMediaService() {
    loadMediaService()
}

actual fun loadLoginSyncModule() {
    loadKoinModules(
        module {
            single { LoginSyncStore(get(), get(), get()) }
            single<LoginSyncSenderRepository> { LoginSyncSenderRepositoryImpl(get(), LoginSyncClient()) }
            // Library sync (#2663): this phone drives every round with the computer it is paired with.
            single { SyncedSettings(get(), get()) }
            single { SyncEngine(get(), get(), SyncApplier(get(), get()), get()) }
            single<LibrarySyncPhoneRepository> {
                LibrarySyncPhoneRepositoryImpl(get(), get(), get(), get(), get(), get(), get(), PairedSyncClient())
            }
            single<LibrarySyncRepository> { get<LibrarySyncPhoneRepository>() }
        },
    )
}

package com.oblivion.nocturne.domain.repository

import com.oblivion.nocturne.domain.data.model.download.DownloadProgress
import kotlinx.coroutines.flow.Flow

interface UpdateDownloader {
    /**
     * Downloads the update APK and returns the local file path.
     * @param url The URL to download the APK from.
     * @return A Flow of [DownloadProgress] representing the progress of the download.
     */
    suspend fun downloadUpdate(url: String): Flow<DownloadProgress>
}

expect class UpdateInstaller {
    /**
     * Installs the update APK from the given local path.
     * @param path The local path to the downloaded APK.
     * @return A Flow of [DownloadProgress] (or similar) representing the progress of the installation.
     *          Since installation is usually a single step with a system dialog, this might just emit Success/Error.
     */
    suspend fun installUpdate(path: String): Flow<DownloadProgress>
}

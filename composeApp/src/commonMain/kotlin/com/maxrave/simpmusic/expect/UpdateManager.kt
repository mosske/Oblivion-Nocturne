package com.maxrave.simpmusic.expect

/**
 * Downloads and installs an APK update.
 * @param url The URL of the APK to download.
 * @param versionTag A tag to use in the filename (e.g., version number or release tag).
 */
expect fun downloadAndInstallUpdate(url: String, versionTag: String)

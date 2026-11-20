package com.oblivion.nocturne.expect

import java.awt.Desktop
import java.net.URI

actual fun openUrl(url: String) {
    if (Desktop.isDesktopSupported()) {
        Desktop.getDesktop().browse(URI(url))
    }
}

actual fun downloadAndInstallUpdate(url: String, versionTag: String) {
    openUrl(url)
}

actual fun shareUrl(
    title: String,
    url: String,
) {
    openUrl(url)
}

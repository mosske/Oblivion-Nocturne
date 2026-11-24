package com.maxrave.simpmusic.expect

import com.oblivion.nocturne.logger.Logger
import multiplatform.network.cmptoast.showToast
import java.awt.Desktop
import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.StringSelection
import java.net.URI

/**
 * Downloads and installs an APK update.
 * 
 * Note: On JVM (Desktop), we just open the URL in the browser for the user to download manually.
 */
actual fun downloadAndInstallUpdate(url: String, versionTag: String) {
    openUrl(url)
}

actual fun openUrl(url: String) {
    if (openWithDesktopApi(url)) return
    if (openWithSystemLauncher(url)) return

    Logger.e("OpenUrl", "Could not open $url by any means")
    showToast("Could not open the link")
}

private fun openWithDesktopApi(url: String): Boolean =
    runCatching {
        if (!Desktop.isDesktopSupported()) return@runCatching false
        val desktop = Desktop.getDesktop()
        if (!desktop.isSupported(Desktop.Action.BROWSE)) return@runCatching false
        desktop.browse(URI(url))
        true
    }.getOrElse { error ->
        Logger.w("OpenUrl", "Desktop.browse failed for $url: ${error.message}")
        false
    }

/**
 * Hands the URL to whatever the platform ships for the job.
 *
 * The Linux list is ordered by how likely each one is to be present: `xdg-open` is the
 * convention but is not guaranteed to be installed, `gio` comes with glib so it is on every
 * desktop that runs GTK at all, and `$BROWSER` is the last thing a minimal setup may still
 * honour. A successful `start()` only proves the binary exists — waiting for an exit code would
 * block the caller (these run on the AWT event thread), so we take that as good enough.
 *
 * Note: On Android (if this were called there), we would use an Intent.
 */
private fun openWithSystemLauncher(url: String): Boolean {
    val os = System.getProperty("os.name", "").lowercase()
    val candidates =
        when {
            os.contains("mac") -> listOf(listOf("open", url))
            os.contains("windows") -> listOf(listOf("rundll32", "url.dll,FileProtocolHandler", url))
            else ->
                listOfNotNull(
                    listOf("xdg-open", url),
                    listOf("gio", "open", url),
                    System.getenv("BROWSER")?.takeIf { it.isNotBlank() }?.let { listOf(it, url) },
                )
        }

    return candidates.any { command ->
        runCatching { ProcessBuilder(command).start() }
            .onFailure { error -> Logger.w("OpenUrl", "${command.first()} failed for $url: ${error.message}") }
            .isSuccess
    }
}

actual fun shareUrl(
    title: String,
    url: String,
) {
    val stringSelection = StringSelection(url)
    val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.setContents(stringSelection, null)
    showToast("Copied to clipboard")
}

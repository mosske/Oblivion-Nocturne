package com.maxrave.simpmusic.expect

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import org.koin.mp.KoinPlatform.getKoin

/**
 * Downloads and installs an APK update.
 * 
 * On Android, we open the URL in the system browser.
 */
actual fun downloadAndInstallUpdate(url: String, versionTag: String) {
    val context: AppCompatActivity = getKoin().get()
    val intent = Intent(
        Intent.ACTION_VIEW,
        url.toUri(),
    )
    intent.setFlags(FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

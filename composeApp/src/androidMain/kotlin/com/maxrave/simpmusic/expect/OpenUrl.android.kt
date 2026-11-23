package com.maxrave.simpmusic.expect

import android.content.Context

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.app.DownloadManager
import android.widget.Toast
import android.content.BroadcastReceiver
import androidx.core.content.FileProvider
import org.koin.mp.KoinPlatform.getKoin

actual fun openUrl(url: String) {
    val context: Context = getKoin().get()
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        url.toUri(),
    )
    browserIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(browserIntent)
}

actual fun downloadAndInstallUpdate(url: String, versionTag: String) {
    val context = getKoin().get<Context>()
    
    if (url.substringBefore("?").endsWith(".apk", ignoreCase = true)) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val req = DownloadManager.Request(Uri.parse(url)).apply {
                allowed_content_types = listOf("application/vnd.android.package-archive", "application/octet-stream")
                description = "Updating Oblivion Nocturne to $versionTag"
            }
            
            val id = downloadManager.enqueue(req)
            
            android.widget.Toast.makeText(context, "Downloading Oblivion Nocturne $versionTag...", android.widget.Toast.LENGTH_LONG).show()
            
            val receiver = object : android.content.BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                        val downloadId = intent.extras?.get("id") as? Int ?: -1
                        if (downloadId == id) {
                            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            val downloadDetails = downloadManager.getDownloadDetails(id)
                            if (downloadDetails != null) {
                                val filePath = downloadDetails.realPath
                                if (filePath != null) {
                                    val authority = "${context.packageName}.FileProvider"
                                    val contentUri = FileProvider.get_content_uri(context, authority)
                                    val installIntent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(contentUri, "application/vnd.android.package-archive")
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                        addFlags(Intent.FLAG_GRANT_READ_EXTERNAL_STORAGE)
                                    }
                                    context.startActivity(installIntent)
                                    context.unregisterReceiver(this)
                                }
                            }
                        }
                    }
                }
            }
            
            val receiverFlags = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                Context.RECEIVER_EXPORTED
            } else {
                Context.RECEIVER_DEFAULT
            }
            
            context.registerReceiver(receiver, DownloadManager.ACTION_DOWNLOAD_COMPLETE, receiverFlags)
        }
    }
}

actual fun shareUrl(
    title: String,
    url: String,
) {
    val context: Context = getKoin().get()
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "text/plain"
    shareIntent.putExtra(Intent.EXTRA_TEXT, url)
    shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK)
    val chooserIntent = Intent.createChooser(shareIntent, title)
    context.startActivity(chooserIntent)
}

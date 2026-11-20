package com.oblivion.nocturne.expect

expect fun openUrl(url: String)

expect fun downloadAndInstallUpdate(url: String, versionTag: String)

expect fun shareUrl(
    title: String,
    url: String,
)
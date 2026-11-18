package com.oblivion.nocturne.kotlinytmusicscraper.models.body

import com.oblivion.nocturne.kotlinytmusicscraper.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class NotificationBody(
    val context: Context,
    val notificationsMenuRequestType: String = "NOTIFICATIONS_MENU_REQUEST_TYPE_INBOX",
)
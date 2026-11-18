package com.oblivion.nocturne.kotlinytmusicscraper.models

import com.oblivion.nocturne.kotlinytmusicscraper.models.subscriptionButton.SubscribeButtonRenderer
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionButton(
    val subscribeButtonRenderer: SubscribeButtonRenderer,
)
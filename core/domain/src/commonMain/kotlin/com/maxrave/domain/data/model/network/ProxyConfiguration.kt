package com.oblivion.nocturne.domain.data.model.network

import com.oblivion.nocturne.domain.manager.DataStoreManager

data class ProxyConfiguration(
    val host: String,
    val port: Int,
    val type: DataStoreManager.ProxyType,
    val username: String? = null,
    val password: String? = null,
)
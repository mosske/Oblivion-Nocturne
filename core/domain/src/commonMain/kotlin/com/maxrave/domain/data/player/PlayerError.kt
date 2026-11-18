package com.oblivion.nocturne.domain.data.player

/**
 * Generic player error wrapper
 */
data class PlayerError(
    val errorCode: Int,
    val errorCodeName: String,
    val message: String?,
)
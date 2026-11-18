package com.oblivion.nocturne.expect

import com.oblivion.nocturne.logger.Logger
import com.oblivion.nocturne.ui.mini_player.MiniPlayerManager

actual fun toggleMiniPlayer() {
    Logger.d("MiniPlayer", "Toggle called, current state: ${MiniPlayerManager.isOpen}")
    MiniPlayerManager.isOpen = !MiniPlayerManager.isOpen
    Logger.d("MiniPlayer", "New state: ${MiniPlayerManager.isOpen}")
}

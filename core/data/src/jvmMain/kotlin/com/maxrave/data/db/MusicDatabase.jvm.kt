package com.oblivion.nocturne.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import com.oblivion.nocturne.common.DB_NAME
import com.oblivion.nocturne.data.io.getHomeFolderPath
import java.io.File

actual fun getDatabaseBuilder(
    converters: Converters
): RoomDatabase.Builder<MusicDatabase> {
    return Room.databaseBuilder<MusicDatabase>(
        name = getDatabasePath()
    ).addTypeConverter(converters)
}

actual fun getDatabasePath(): String {
    val dbFile = File(getHomeFolderPath(listOf(".oblivionnocturne", "db")), DB_NAME)
    return dbFile.absolutePath
}
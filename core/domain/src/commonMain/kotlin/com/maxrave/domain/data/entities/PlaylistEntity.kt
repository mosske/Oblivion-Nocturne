package com.oblivion.nocturne.domain.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.oblivion.nocturne.domain.data.type.PlaylistType
import com.oblivion.nocturne.domain.data.type.RecentlyType
import com.oblivion.nocturne.domain.extension.now
import com.oblivion.nocturne.domain.utils.isRadioPlaylistId
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    val author: String? = "",
    val description: String = "",
    val duration: String = "",
    val durationSeconds: Int = 0,
    val privacy: String = "PRIVATE",
    val thumbnails: String = "",
    val title: String,
    val trackCount: Int = 0,
    val tracks: List<String>? = null,
    val year: String? = null,
    val liked: Boolean = false,
    val inLibrary: LocalDateTime = now(),
    val favoriteAt: LocalDateTime? = now(),
    val downloadedAt: LocalDateTime? = now(),
    val downloadState: Int = DownloadState.STATE_NOT_DOWNLOADED,
) : PlaylistType,
    RecentlyType {
    override fun playlistType(): PlaylistType.Type =
        if (id.isRadioPlaylistId()) {
            PlaylistType.Type.RADIO
        } else {
            PlaylistType.Type.YOUTUBE_PLAYLIST
        }

    override fun objectType(): RecentlyType.Type = RecentlyType.Type.PLAYLIST
}
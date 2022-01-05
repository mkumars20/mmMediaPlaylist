package com.example.mediaplaylist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class mediaType {
    VIDEO, AUDIO, IMAGE, GIF
}

@Entity(tableName = "media_playlist_table")
data class MediaPlaylistItem constructor(
    @PrimaryKey(autoGenerate = true)
    var mediaIndex: Long = 0L,
    @ColumnInfo(name = "media_url")
    val mediaUrl: String,
    @ColumnInfo(name = "media_type")
    var m_mediaType: String,
    @ColumnInfo(name = "media_name")
    val m_mediaName: String,
    @ColumnInfo(name = "media_description")
    val m_mediaDesc: String,
    @ColumnInfo(name = "media_art")
    val m_mediaArt: String)


/*
@Entity
data class DatabaseVideo constructor(
    @PrimaryKey
    val url: String,
    val updated: String,
    val title: String,
    val description: String,
    val thumbnail: String)
*/
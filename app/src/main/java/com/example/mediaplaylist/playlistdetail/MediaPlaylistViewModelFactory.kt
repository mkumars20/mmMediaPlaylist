package com.example.mediaplaylist.playlistdetail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mediaplaylist.database.MediaPlaylistDatabasedao


class MediaPlaylistViewModelFactory(
    private val dataSource: MediaPlaylistDatabasedao,
    private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaPlaylistViewModel::class.java)) {
            return MediaPlaylistViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

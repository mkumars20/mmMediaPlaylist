package com.example.mediaplaylist.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.mediaplaylist.database.MediaPlaylistDatabase
import com.example.mediaplaylist.database.MediaPlaylistItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaPlaylistRepository(private val database: MediaPlaylistDatabase) {

      val videos: LiveData<List<MediaPlaylistItem>> = database.mediaplaylistDao.getPlaylist()
//    val videos: List<MediaPlaylistItem> = database.mediaplaylistDao.getPlaylist()


    /*
    suspend fun getPlaylist() : List<MediaPlaylistItem> {
        withContext(Dispatchers.IO) {
            videos = database.mediaplaylistDao.getPlaylist()
        }
        return videos
    }
    */

    fun insert(item: MediaPlaylistItem ) {
        database.mediaplaylistDao.insert(item)
    }

    fun update(item: MediaPlaylistItem ) {
        database.mediaplaylistDao.update(item)
    }

    fun get(mIndex: Long? ) :MediaPlaylistItem {
        return database.mediaplaylistDao.get(mIndex)
    }

    fun getPrimaryKey(): Long {
        return database.mediaplaylistDao.getPrimaryKey()
    }

    fun getTotalItems(): Long {
        return database.mediaplaylistDao.getTotalItems()
    }

    fun getNextItem(mIndex: Long? ) :MediaPlaylistItem {
        return database.mediaplaylistDao.getNextItem(mIndex)
    }

    fun getPrevItem(mIndex: Long? ) :MediaPlaylistItem {
        return database.mediaplaylistDao.getPrevItem(mIndex)
    }

    fun getLastMediaPlaylistItem():MediaPlaylistItem? {
        return database.mediaplaylistDao.getLastMediaPlaylistItem()
    }

    fun getFullPlaylist(): List<MediaPlaylistItem> {
        return database.mediaplaylistDao.getFullPlaylist()
    }

    fun remove(id: Long) {
        database.mediaplaylistDao.remove(id)
    }

    fun clearAll() {
        database.mediaplaylistDao.clear()
    }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
        suspend fun refreshVideos() {
            withContext(Dispatchers.IO) {
                val playlist = DevByteNetwork.devbytes.getPlaylist()
                database.videoDao.insertAll(playlist.asDatabaseModel())
            }
        }
    */
}
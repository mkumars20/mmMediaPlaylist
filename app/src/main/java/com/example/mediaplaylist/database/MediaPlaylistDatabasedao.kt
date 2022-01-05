package com.example.mediaplaylist.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface MediaPlaylistDatabasedao {

    @Insert
    fun insert(mediaitem: MediaPlaylistItem)

    @Query("SELECT mediaIndex FROM media_playlist_table   ORDER BY mediaIndex ASC  LIMIT 1")
    fun getPrimaryKey(): Long

    @Query("SELECT COUNT(mediaIndex) FROM media_playlist_table")
    fun getTotalItems(): Long

    @Query("DELETE FROM media_playlist_table WHERE mediaIndex = :id")
    fun remove(id: Long)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param MediaItem new value to write
     */
    @Update
    fun update(mItem: MediaPlaylistItem)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from media_playlist_table WHERE mediaIndex = :key")
    fun get(key: Long?): MediaPlaylistItem


    //@Query("select * from media_playlist_table where mediaIndex = (select min(mediaIndex) from media_playlist_table where mediaIndex > :key)")
    @Query("SELECT * from media_playlist_table WHERE mediaIndex > :key ORDER BY mediaIndex LIMIT 1")
    fun getNextItem(key: Long?): MediaPlaylistItem

    //@Query("select * from media_playlist_table where mediaIndex = (select min(mediaIndex) from media_playlist_table where mediaIndex > :key)")
    @Query("SELECT * from media_playlist_table WHERE mediaIndex < :key ORDER BY mediaIndex LIMIT 1")
    fun getPrevItem(key: Long?): MediaPlaylistItem


    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM media_playlist_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM media_playlist_table ORDER BY mediaIndex DESC")
     fun getPlaylist(): LiveData<List<MediaPlaylistItem>>
//   fun getPlaylist(): List<MediaPlaylistItem>


    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM media_playlist_table ORDER BY mediaIndex DESC")
    fun getFullPlaylist(): List<MediaPlaylistItem>
//   fun getPlaylist(): List<MediaPlaylistItem>

    /**
     * Selects and returns the latest playlistmediaItem.
     */
    @Query("SELECT * FROM media_playlist_table ORDER BY mediaIndex DESC LIMIT 1")
    fun getLastMediaPlaylistItem(): MediaPlaylistItem?

}
package com.example.mediaplaylist.playlistdetail

import android.app.Application
import android.view.SurfaceView

enum class playerStateVal {
    INIT, PLAYING, PAUSED, STOPPED
}

enum class mediaTypeVal {
    AUDIO, VIDEO
}

class MediaPlaylistPlayer(application: Application) {

    // property (data member)
    private var mediaUrl: String = "false"
    private var playerState: playerStateVal = playerStateVal.INIT
    private var mediaType: mediaTypeVal = mediaTypeVal.AUDIO

    private var funcCallback: FunctionalInterface? = null

    // member function
    fun prepare(mediaType: mediaTypeVal) {
        // TODO prepare to play audio or video based on the mediatype
    }

    // member function
    fun setDataSource(Uri: String, mtype: mediaTypeVal) {
        mediaUrl = Uri
        mediaType = mtype
    }

    fun setVideoIDandAPIkey(Uri: String, apikey: String) {

    }

    // member function
    fun getPlaystate(): playerStateVal {
        return playerState
    }


    // member function
    fun play(mAVItemUrl: String?) {
        // TODO
    }

    // member function
    fun pause() {
        // TODO
    }

    // member function
    fun stop() {
        // TODO
    }

    public fun getCurrentPosition(): Long
    {
        val position: Long = 1
        // TODO
        return position
    }

    public fun getDuration(): Long
    {
        val duration: Long = 1
        // TODO
        return duration
    }

     public fun seekTo(posn: Long): Boolean
     {
         // TODO
         return true
     }

     public fun setSurfaceView( surfaceview: SurfaceView) : Boolean
     {
         // TODO
         return true
     }

     public fun isInitialized() : Boolean
     {
        // TODO
        return true
     }

     fun release() {
         // TODO
     }
}
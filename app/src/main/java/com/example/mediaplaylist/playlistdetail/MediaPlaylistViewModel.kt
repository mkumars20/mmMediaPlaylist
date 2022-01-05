package com.example.mediaplaylist.playlistdetail

import android.app.Application
import android.content.Context
import com.example.mediaplaylist.database.MediaPlaylistDatabasedao
import com.example.mediaplaylist.database.MediaPlaylistItem
import kotlinx.coroutines.*

import android.net.Uri
import androidx.lifecycle.*
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.util.Util
import android.content.res.AssetFileDescriptor
import com.example.mediaplaylist.repository.MediaPlaylistRepository

import com.example.mediaplaylist.database.MediaPlaylistDatabase


//import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.upstream.*
import android.media.MediaMetadataRetriever
import android.webkit.MimeTypeMap
import com.example.mediaplaylist.database.mediaType
import java.io.File
import java.io.FileInputStream


class MediaPlaylistViewModel(
        dataSource: MediaPlaylistDatabasedao,
        application: Application
    //) : ViewModel() {
    )
    : AndroidViewModel(application)
    , LifecycleObserver {

         val database = dataSource
         private val mediaPlaylistRepository = MediaPlaylistRepository(MediaPlaylistDatabase.getInstance(application))

         /**
         * viewModelJob allows us to cancel all coroutines started by this ViewModel.
         */
         private var viewModelJob = Job()

         /**
         * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
         *
         * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
         * by calling `viewModelJob.cancel()`
         *
         * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
         * the main thread on Android. This is a sensible default because most coroutines started by
         * a [ViewModel] update the UI after performing some processing.
         */
          private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

          var currMediaItem :MediaPlaylistItem? = null

          private var mMediaPlaylistCount : Long = 0

          private var currMediaItemIndex : Long? = 0

          private var addMediaItemIndex : Long = 0

          private var playlistTemp : List<MediaPlaylistItem> = listOf()

          var playlistMemoryFast =  mutableMapOf<Long?, MediaPlaylistItem?>()

          val playlist = mediaPlaylistRepository.videos  // Original

          // Custom Media Player
          private val _player = MutableLiveData<MediaPlaylistPlayer?>()
          val player: LiveData<MediaPlaylistPlayer?> get() = _player
          //player = ExoPlayerFactory.newSimpleInstance(getApplication())

          init {
               initializePlaylistParams()
                ProcessLifecycleOwner.get().lifecycle.addObserver(this)
          }

        private fun initializePlaylistParams() {
            uiScope.launch {
                playlistTemp = getFullPlaylist()
                playlistTemp.forEach {
                    println(it)
                    playlistMemoryFast[it.mediaIndex] = it
                }
            }

            uiScope.launch {
                mMediaPlaylistCount = getTotalMediaPlaylistItems()
            }
        }


        @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onForegrounded() {
                setUpPlayer()
            }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onBackgrounded() {
                releasePlayer()
           }

        private fun setUpPlayer() {

            val dataSourceFactory = DefaultDataSourceFactory(
                getApplication(),
            Util.getUserAgent(getApplication(), "demo")
        )

        //val player = ExoPlayerFactory.newSimpleInstance(getApplication())

        val player = MediaPlaylistPlayer(getApplication())

        /*
        var afd: AssetFileDescriptor
        val context = getApplication<Application>().applicationContext
        afd =   context.getAssets().openFd("theme1.mp3");

        val dataSourceFactoryAudio: DataSource.Factory = object : DataSource.Factory
        {
           override fun createDataSource(): DataSource
          {
                 return AssetDataSource(context)
          }
        }
        val audioSource = ExtractorMediaSource(Uri.parse("assets:///theme1.mp3"), dataSourceFactoryAudio, DefaultExtractorsFactory(), null, null)
        */

        this._player.value = player
    }

        private fun releasePlayer() {
            val player = _player.value ?: return
            player.release()
            this._player.value = null
        }

        /**
         *  Handling the case of the stopped app or forgotten recording,
         *  the start and end times will be the same.j
         *
         *  If the start time and end time are not the same, then we do not have an unfinished
         *  recording.
         */
        private suspend fun getLastMediaPlaylistItem(): MediaPlaylistItem? {
            return withContext(Dispatchers.IO) {
                // var m_mediaPlaylistItem = database.getLastMediaPlaylistItem()
                var m_mediaPlaylistItem = mediaPlaylistRepository.getLastMediaPlaylistItem()
                m_mediaPlaylistItem
            }
        }

        private suspend fun getPrimaryKey(): Long {
            return withContext(Dispatchers.IO) {
                //var m_mediaPlaylistIndx = database.getPrimaryKey()
                var m_mediaPlaylistIndx = mediaPlaylistRepository.getPrimaryKey()
                m_mediaPlaylistIndx
            }
        }

        private suspend fun getTotalMediaPlaylistItems(): Long {
            return withContext(Dispatchers.IO) {
                mMediaPlaylistCount = mediaPlaylistRepository.getTotalItems()
                mMediaPlaylistCount
            }
        }

        private suspend fun getFullPlaylist(): List<MediaPlaylistItem> {
            return withContext(Dispatchers.IO) {
                // var m_mediaPlaylistItem = database.getLastMediaPlaylistItem()
                var m_mediaPlaylist = mediaPlaylistRepository.getFullPlaylist()
                m_mediaPlaylist
            }
        }

         suspend fun insert(mPlstItem: MediaPlaylistItem) {
            withContext(Dispatchers.IO) {
                mediaPlaylistRepository.insert(mPlstItem)
            }
        }


        suspend fun remove(id: Long) {
            withContext(Dispatchers.IO) {
                 mediaPlaylistRepository.remove(id)
            }
        }

        suspend fun update(mPlstItem: MediaPlaylistItem) {
            withContext(Dispatchers.IO) {
                mediaPlaylistRepository.update(mPlstItem)
            }
        }

        suspend fun getAt(mIndex: Long?): MediaPlaylistItem {
            return withContext(Dispatchers.IO) {
                var m_mediaPlaylistItem :MediaPlaylistItem = mediaPlaylistRepository.get(mIndex)
                m_mediaPlaylistItem
            }
        }

        suspend fun getNextAt(mIndex: Long?): MediaPlaylistItem {
            return withContext(Dispatchers.IO) {
                 //var m_mediaPlaylistItem :MediaPlaylistItem = database.getNextItem(mIndex)
                var m_mediaPlaylistItem :MediaPlaylistItem = mediaPlaylistRepository.getNextItem(mIndex)
                m_mediaPlaylistItem
            }
        }

        suspend fun getPreviousAt(mIndex: Long?): MediaPlaylistItem {
            return withContext(Dispatchers.IO) {
                //var m_mediaPlaylistItem :MediaPlaylistItem = database.getNextItem(mIndex)
                var m_mediaPlaylistItem :MediaPlaylistItem = mediaPlaylistRepository.getPrevItem(mIndex)
                m_mediaPlaylistItem
            }
        }


        suspend fun clear() {
            withContext(Dispatchers.IO) {
                mediaPlaylistRepository.clearAll()
            }
        }


        fun addMediaItem(mPlstItem: MediaPlaylistItem) {
            uiScope.launch {
                    //database.insert(mPlstItem)
                    insert(mPlstItem)
                    currMediaItem = getLastMediaPlaylistItem()
                playlistMemoryFast[currMediaItem?.mediaIndex] = currMediaItem
            }
            print(playlistMemoryFast.size)
        }


        /*
        fun onAdd() {

            var a:String = addMediaItemIndex.toString()
            val b = "www.hello.com"
            var mediaUrl2: String = a.plus(b)
            var mMediaType: String = "Audio"
            var mMediaName: String = "Local Image"
            addMediaItemIndex++

            if(addMediaItemIndex.mod(2L) == 0L) {
                mediaUrl2 = "screenshot_2020-01-24-14-37-12.png"
                mMediaType = "LocalImage"
                mMediaName = "Disk Local Image Disk"
            }
            else if(addMediaItemIndex.mod(3L) == 0L) {
                mediaUrl2 = "https://i.imgur.com/1ALnB2s.gif"
                mMediaType = "ServerImage"
                mMediaName = "Remote GIF Image "
            }

            val mPlstItem = MediaPlaylistItem(0 , mediaUrl2, mMediaType, mMediaName, "Unkonown", "Unkonwn")

            print(playlistMemoryFast.size)

            uiScope.launch {
                //database.insert(mPlstItem)
                insert(mPlstItem)
                currMediaItem = getLastMediaPlaylistItem()
                playlistMemoryFast[currMediaItem?.mediaIndex] = currMediaItem
            }
            print(playlistMemoryFast.size)
        }
        */


        fun removeMediaItem(index: Long) {
                uiScope.launch {
                    remove(index)
                    if( playlistMemoryFast[index] != null)
                        playlistMemoryFast.remove(index)
                }
        }

        /**
        * Executes when the START button is clicked.
        */
        fun onNextItem(): Boolean {

            var mPlaylistItm: MediaPlaylistItem? = null
            var isNextItemValid: Boolean = true

            uiScope.launch {
                print(currMediaItemIndex)
                print(mMediaPlaylistCount)

                mPlaylistItm = getNextAt(currMediaItemIndex)

                if (mPlaylistItm == null) {
                    isNextItemValid = false
                    println("invalid")
                }
            }

            if(isNextItemValid == true)
                return(playItem(mPlaylistItm))

            return isNextItemValid
        }

        /**
        * Executes when the START button is clicked.
        */
        fun onPrevItem(): Boolean {

            var mPlaylistItm: MediaPlaylistItem? = null
            var isPreviousItemValid: Boolean = true

            uiScope.launch {
                print(currMediaItemIndex)
                print(mMediaPlaylistCount)

                mPlaylistItm = getPreviousAt(currMediaItemIndex)

                if (mPlaylistItm == null) {
                    isPreviousItemValid = false
                    println("invalid")
                }
            }

            if(isPreviousItemValid == true)
                return(playItem(mPlaylistItm))

            return isPreviousItemValid
        }


        /**
         * Executes when the play is called with the index.
        */
        fun playItem(mIndex: Long?) : Boolean {

            var mPlaylistItm: MediaPlaylistItem? = null
            var isMediaCurrent: Boolean = false
            var mplayerState :playerStateVal? = playerStateVal.STOPPED

            if(currMediaItemIndex == mIndex)  // Check if the the selected media is already playing
            {
                if (_player.value?.isInitialized() == true)
                    mplayerState = _player.value?.getPlaystate()

                if (mplayerState == playerStateVal.PLAYING)  return true
            }

            uiScope.launch {
                    mPlaylistItm = getAt(mIndex)
            }

            return playItem(mPlaylistItm)
        }

        /**
        * Executes when the play is called with the mediaitem object.
        */
        fun playItem(mPlstItem: MediaPlaylistItem?) : Boolean {

           var mplayerState :playerStateVal? = playerStateVal.STOPPED
           var isMediaCurrent: Boolean = false
           var mPlaylistItm: MediaPlaylistItem? = null

           uiScope.launch {
               mPlaylistItm = getAt(currMediaItemIndex)
               if (mPlaylistItm == mPlstItem)
                   isMediaCurrent = true
           }

           if(isMediaCurrent == true) {
               if (_player.value?.isInitialized() == true)
                    mplayerState = _player.value?.getPlaystate()

               if (mplayerState == playerStateVal.PLAYING) return true
           }

           if ((_player.value?.isInitialized() == true) && _player.value?.getPlaystate() == playerStateVal.PLAYING) {
                 _player.value?.stop()
           }

           var mediaType: String? = mPlaylistItm?.m_mediaType

           if (mediaType == "audio")
                _player.value?.prepare(mediaTypeVal.AUDIO)
            else if (mediaType == "video")
                  _player.value?.prepare(mediaTypeVal.VIDEO)

            var mAVItemUrl: String? = mPlaylistItm?.mediaUrl
            _player.value?.play(mAVItemUrl);

           currMediaItemIndex = mPlaylistItm?.mediaIndex
           currMediaItem = mPlaylistItm

           return true
        }

        /**
        * Executes when the pause  is called with an index.
        */
        //fun pauseItem(mPlstItem: MediaPlaylistItem?) : Boolean {
        fun pauseItem(mIndex: Long?) : Boolean {

            var mplayerState :playerStateVal? = playerStateVal.STOPPED
            var isPauseSuccess: Boolean = false

            if(currMediaItemIndex == mIndex)  // Check if the the selected media is  playing
            {
                if (_player.value?.isInitialized() == true)
                    mplayerState = _player.value?.getPlaystate()

                if (mplayerState == playerStateVal.PAUSED) isPauseSuccess = true
                else if (mplayerState == playerStateVal.PLAYING) {
                    _player.value?.pause()
                    isPauseSuccess = true
                }
            }
            return isPauseSuccess
        }

        /**
        * Executes when the START button is clicked.
        */
        fun pauseItem(mPlstItem: MediaPlaylistItem?) : Boolean {

            var mplayerState :playerStateVal? = playerStateVal.STOPPED
            var isMediaCurrent: Boolean = false
            var mPlaylistItm: MediaPlaylistItem? = null
            var isPauseSuccess: Boolean = false

            uiScope.launch {
                mPlaylistItm = getAt(currMediaItemIndex)
                if (mPlaylistItm == mPlstItem)
                    isMediaCurrent = true
            }

            if(isMediaCurrent == true) {
                if (_player.value?.isInitialized() == true)
                    mplayerState = _player.value?.getPlaystate()

                if (mplayerState == playerStateVal.PAUSED) isPauseSuccess = true
                else if (mplayerState == playerStateVal.PLAYING) {
                    _player.value?.pause()
                    isPauseSuccess = true
                }
            }
            return isPauseSuccess
        }


        fun getItemAtIndex(mIndex: Long): MediaPlaylistItem? {
            //var mPlaylistItm: MediaPlaylistItem
            uiScope.launch {
                //currMediaItem = getAt(mIndex)
            }
            val tstItem: MediaPlaylistItem?  = playlistMemoryFast[mIndex]
            return tstItem
        }

        /**
         * Executes when the CLEAR button is clicked.
         */
        fun onClear() {
            uiScope.launch {
                // Clear the database table.
                clear()
            }
        }

        /**
        * Executes when the CLEAR button is clicked.
        */
        fun onGetAll() : LiveData<List<MediaPlaylistItem>>{
            uiScope.launch {
            }
            return mediaPlaylistRepository.videos
        }


        fun getDuration() : Long?{
           return  _player.value?.getDuration()
        }

        fun getPosition() : Long?{
            return  _player.value?.getCurrentPosition()
        }

        fun seekTo(posn: Long) : Boolean?{
            if(posn < 0) return false
            else return  _player.value?.seekTo(posn)
        }

        /**
         * Called when the ViewModel is dismantled.
         * At this point, we want to cancel all coroutines;
         * otherwise we end up with processes that have nowhere to return to
         * using memory and resources.
         */
        override fun onCleared() {
            super.onCleared()
            viewModelJob.cancel()
        }
    }
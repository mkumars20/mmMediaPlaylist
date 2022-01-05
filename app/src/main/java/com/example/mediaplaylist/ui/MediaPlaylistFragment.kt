package com.example.mediaplaylist.ui

import android.annotation.TargetApi
import android.app.Activity
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mediaplaylist.R
import com.example.mediaplaylist.database.MediaPlaylistDatabase
import com.example.mediaplaylist.databinding.MediaPlaylistFragmentBinding
//import android.R
import kotlinx.android.synthetic.main.media_playlist_fragment.*
//import com.example.signlanguage.databinding.FragmentYoutubeBinding
import android.os.Environment
import android.widget.Button
import androidx.annotation.IdRes
import androidx.core.app.ActivityCompat
import com.example.mediaplaylist.databinding.ActivityMainBinding.bind
import com.example.mediaplaylist.playlistdetail.MediaPlaylistAdapter
import com.example.mediaplaylist.playlistdetail.MediaPlaylistListener
import com.example.mediaplaylist.playlistdetail.MediaPlaylistViewModel
import com.example.mediaplaylist.playlistdetail.MediaPlaylistViewModelFactory


import java.io.File

class MediaPlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = MediaPlaylistFragment()
    }
    private lateinit var viewModel: MediaPlaylistViewModel
    val api_key: String  =  "AIzaSyAOPuU8JkYfA5oEb_NRaEaF-Hhkmd_a4vg"


    fun shouldAskPermissions(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    @TargetApi(23)
    fun askPermissions() {
        val permissions = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
        val requestCode = 200
//        ActivityCompat.requestPermissions(this.pare, permissions, requestCode)
        requestPermissions(permissions, requestCode)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Get a reference to the binding object and inflate the fragment views.
        val binding: MediaPlaylistFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.media_playlist_fragment, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = MediaPlaylistDatabase.getInstance(application).mediaplaylistDao

        val viewModelFactory = MediaPlaylistViewModelFactory(dataSource, application)

        // Get a reference to the ViewModel associated with this fragment.
        val mediaPlaylistViewModel =
            ViewModelProvider(
                this, viewModelFactory).get(MediaPlaylistViewModel::class.java)

        // To use the View Model with data binding, you have to explicitly
        // give the binding object a reference to it.

        binding.mediaPlaylistViewModel = mediaPlaylistViewModel

        binding.setLifecycleOwner(this)

        binding.root.findViewById<RecyclerView>(R.id.play_list).apply {
            layoutManager = LinearLayoutManager(context)
        }

        mediaPlaylistViewModel.player.observe(viewLifecycleOwner, Observer {
            //player_view2.player = it
            //binding.playerView.player = it
        })


        val adapter = MediaPlaylistAdapter(MediaPlaylistListener { mediaIndex ->

            mediaPlaylistViewModel.playItem(mediaIndex)

            var media = "https://i.imgur.com/1ALnB2s.gif"

            // Testing Remove on Click
            //mediaPlaylistViewModel.onRemove(mediaIndex)

            // loading image testing
            val mPlylstItem = mediaPlaylistViewModel.getItemAtIndex(mediaIndex)
            if(mPlylstItem?.m_mediaType == "LocalImage") {



                if (shouldAskPermissions()) {
                    askPermissions();
                }


                val completePath = Environment.getExternalStorageDirectory()
                    .toString() + "/Pictures/ScreenShots/" + mPlylstItem.mediaUrl
                val file = File(completePath)
                val imageUri: Uri = Uri.fromFile(file)
                if (media !== null) {
                    Glide.with(this)
                        .load(imageUri)
                        .into(playlistImageView)
                } else {
                    playlistImageView.setImageResource(R.drawable.ic_launcher_background)
                }
            }
            else if(mPlylstItem?.m_mediaType == "ServerImage") {
                val media2 = mPlylstItem.mediaUrl

                if (media2 !== null) {
                    Glide.with(this)
                        //.asGif()
                        .load(media2)
                        .into(playlistImageView)
                } else {
                    playlistImageView.setImageResource(R.drawable.ic_launcher_background)
                }

            }
            else
                playlistImageView.setImageResource(R.drawable.ic_launcher_background)

        })

        binding.playList.adapter = adapter

        mediaPlaylistViewModel.playlist.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })


        /*
        // Get reference to the view of Video player
        val ytPlayer = findViewById<YouTubePlayerView>(R.id.ytPlayer)

        ytPlayer.initialize(api_key, object : YouTubePlayer.OnInitializedListener{
            // Implement two methods by clicking on red error bulb
            // inside onInitializationSuccess method
            // add the video link or the
            // playlist link that you want to play
            // In here we also handle the play and pause functionality
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.loadVideo("HzeK7g8cD0Y")
                player?.play()
            }

            // Inside onInitializationFailure
            // implement the failure functionality
            // Here we will show toast
            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                //Toast.makeText(this , "Video player Failed" , Toast.LENGTH_SHORT).show()
            }
        })
        */

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MediaPlaylistViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

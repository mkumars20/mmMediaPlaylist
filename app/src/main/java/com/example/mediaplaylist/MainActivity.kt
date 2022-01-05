package com.example.mediaplaylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubeBaseActivity

import com.google.android.youtube.player.YouTubePlayerSupportFragment
import androidx.core.app.ActivityCompat.requestPermissions

import android.annotation.TargetApi

import android.os.Build
import androidx.core.app.ActivityCompat




class MainActivity : AppCompatActivity() {

      val api_key: String  =  "AIzaSyAOPuU8JkYfA5oEb_NRaEaF-Hhkmd_a4vg"
//    val api_key: String  =  "AIzaSyAmIWlsx0vSv9S-CUq0evvQMxv76XvzgtE"

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)

        }
}
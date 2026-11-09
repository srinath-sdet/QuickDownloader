package com.example.quickdownloader

import android.app.Application
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.ffmpeg.FFmpeg

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            YoutubeDL.getInstance().init(this)
            FFmpeg.getInstance().init(this)
            Log.d("QuickDownloader", "YoutubeDL and FFmpeg initialized successfully")
        } catch (e: Exception) {
            Log.e("QuickDownloader", "Failed to init YoutubeDL", e)
        }
    }
}

package com.example.inception.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import com.example.inception.constant.ACTION_CREATE
import com.example.inception.constant.ACTION_PLAY
import com.example.inception.constant.ACTION_STOP
import com.example.inception.constant.CURRENT_PLAYED_SONG


class AudioPlayerService : Service() ,
MediaPlayer.OnPreparedListener,
MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener{

    private var MediaPlayer: MediaPlayer? = null

    fun init() {
        MediaPlayer = MediaPlayer()
        MediaPlayer?.setOnPreparedListener(this)
        MediaPlayer?.setOnCompletionListener(this)

        MediaPlayer?.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
        )
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            var actionIntent = intent.action
            when (actionIntent) {
                ACTION_CREATE -> init()
                ACTION_PLAY -> {
                    MediaPlayer?.run {
                        reset()
                        setDataSource(intent.getStringExtra(CURRENT_PLAYED_SONG))
                        prepareAsync()
                    }
                }
                ACTION_STOP -> MediaPlayer?.stop()
            }
        }
        return flags
    }

    override fun onPrepared(mp: MediaPlayer?) {
        MediaPlayer?.start()
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Toast.makeText(this, "Error Read File", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        Toast.makeText(this, "Player Stop", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayer?.release()
    }
}
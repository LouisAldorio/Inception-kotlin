package com.example.inception.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import com.example.inception.constant.*


class AudioPlayerService : Service() ,
MediaPlayer.OnPreparedListener,
MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener{

    private var MediaPlayer: MediaPlayer? = null

    private var currentPlayedSongIndex = 0

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
                    currentPlayedSongIndex = intent.getIntExtra(CURRENT_PLAYED_SONG_INDEX,0)
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
        val intent = Intent(AUDIO_FINISH_PLAY)
        intent.putExtra(CURRENT_PLAYED_SONG_INDEX,currentPlayedSongIndex)
        sendBroadcast(intent)
        Toast.makeText(this, "Player Stop", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        MediaPlayer?.release()
    }
}
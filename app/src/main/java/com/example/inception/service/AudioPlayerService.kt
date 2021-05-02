package com.example.inception.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.widget.Toast
import com.example.inception.constant.*

//pada media player, terlebih dahulu kita akan membuat media player berjalan di background dengan service, agar tidak terjadi ANR
// kita implementasikan saja langsung semua listener yang ktia perlukan untuk mejalankan media player
class AudioPlayerService : Service() ,
MediaPlayer.OnPreparedListener,
MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener{

    //inisiasi media player
    private var MediaPlayer: MediaPlayer? = null

    //inisiasi variable yang nantinya akan menampung, index dari lagu yang sedang diputar
    private var currentPlayedSongIndex = 0

    //init disini akan dijalankan ketika objek dari kelas ini diinisialisasi/ menerima sinyal create dari luar kelas
    fun init() {
        //inisiasi media player
        MediaPlayer = MediaPlayer()
        //masukan listener yang diperlukan, yang sebelumnya telah kita implemetasikan langsung pada kelasnya
        MediaPlayer?.setOnPreparedListener(this)
        MediaPlayer?.setOnCompletionListener(this)

        //tentukan konfigurasi dari media player
        MediaPlayer?.setAudioAttributes(
            //set content type yang ingin dijalankan, yaitu adalah music / MP3
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    //disini kita Menyetel atribut yang menjelaskan tujuan penggunaan sinyal audio, seperti alarm atau nada dering, kita menggunakan media.
                .setUsage(AudioAttributes.USAGE_MEDIA)
                    //lalu build
                .build()
        )
    }

    //kita return null, karena kita tidak ingin service terikat ke komponen manapun
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    //pada onstartCommand akan kita tentukan aksi yang akan dilakukan oleh media player
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //check terlebih dahulu apakah ada intent
        if (intent != null) {
            //jika intent ada, maka tangkap action yang dikirimkan didalam intent, untuk mengtahui aksi apa yang harus dilakukan
            var actionIntent = intent.action
            when (actionIntent) {
                //jika menerina action untuk create, maka kita tinggal panggil fungsi init yang telah ktia buat sebelumnya
                ACTION_CREATE -> init()
                //ketika menerima action untuk play
                ACTION_PLAY -> {
                    //ambil index dari lagu yang ingin di mainkan, dan simpan kedalam variable yang telah kita declare sebelumnya
                    currentPlayedSongIndex = intent.getIntExtra(CURRENT_PLAYED_SONG_INDEX,0)
                    //jalankan beberapa konfigurasi pada media player
                    MediaPlayer?.run {
                        //setiap audio ingin di play kita akan mereset state audio ke awal, sehingga lagu akan selalu diputar dari awal
                        reset()
                        //disini kita akan memberikan sumber, darimana sumber daya file mp3 akan diambil
                        // kami akan melakukan load dari server(digital ocean droplet), dimana file mp3 sudah di serve
                        setDataSource(intent.getStringExtra(CURRENT_PLAYED_SONG))
                        //persiapkan untuk memutar, tetapi secara async karena proses load sumber daya dari server dapat memakan waktu
                        //hal ini dapat menghindarkan kita dari ANR
                        prepareAsync()
                    }
                }
                //action stop akan menuruh media player untuk berhenti memainkan lagu yang sedang diputar saat ini
                ACTION_STOP -> MediaPlayer?.stop()
            }
        }
        return flags
    }

    //jika persiapan sudah selesai(load file sumber daya), langsung putar lagu
    override fun onPrepared(mp: MediaPlayer?) {
        MediaPlayer?.start()
    }

    //jika error dalam membaca file, kita akan throw error
    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        Toast.makeText(this, "Error Read File", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        //ketika lagu baru selesai diputar
        val intent = Intent(AUDIO_FINISH_PLAY)
        //kita akan mengirimkan broadcast ke recycle view untuk mengupdate button diplay menjadi button play pada lagu yang baru selesai diputar, yang awalnya button pause
        //hal ini bertujuan untuk meningkatkan user experience
        intent.putExtra(CURRENT_PLAYED_SONG_INDEX,currentPlayedSongIndex)
        sendBroadcast(intent)
        Toast.makeText(this, "Player Stop", Toast.LENGTH_SHORT).show()
    }

    //jika sudah selesai, memainkan lagu, maka kita harus memanggil release agar tidak memakan memori
    override fun onDestroy() {
        super.onDestroy()
        MediaPlayer?.release()
    }
}
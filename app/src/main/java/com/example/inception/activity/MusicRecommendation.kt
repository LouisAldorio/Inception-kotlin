package com.example.inception.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inception.R
import com.example.inception.adaptor.AudioSongRecycleViewAdapter
import com.example.inception.constant.*
import com.example.inception.data.CovidStatistic
import com.example.inception.data.Song
import com.example.inception.service.AudioPlayerService
import kotlinx.android.synthetic.main.activity_covid.*
import kotlinx.android.synthetic.main.activity_music_recommendation.*
import java.text.SimpleDateFormat
import java.util.*


var IntentService : Intent? = null
class MusicRecommendation : AppCompatActivity() {
    var adapterAudio : AudioSongRecycleViewAdapter? = null

    private val AudioFinishedReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val index = intent?.getIntExtra(CURRENT_PLAYED_SONG_INDEX,0)
            adapterAudio?.notifyItemChanged(index!!, AUDIO_RV_PAYLOAD)
        }
    }


    val songs = mutableListOf<Song>(
        Song(
            "Something Just Like This",
            "Coldplay",
            "http://128.199.174.165:8081/photo/something-543197503.jpg",
            "http://128.199.174.165:8081/audio/Coldplay-SomethingJustLikeThis-393929056.mp3"
        ),
        Song(
            "芒種-MangZhong",
            "音闕詩聽",
            "http://128.199.174.165:8081/photo/Web-BNMC-mang-zhong-222683593.png",
            "http://128.199.174.165:8081/audio/音闕詩聽–芒種-MangZhong-120900498.mp3"
        ),
        Song(
            "FutariNoKimochi",
            "Inuyasha",
            "http://128.199.174.165:8081/photo/futari-994887907.jpg",
            "http://128.199.174.165:8081/audio/Inuyasha-FutariNoKimochi-822964372.mp3"
        ),
        Song(
            "Gang hao yu jian ni",
            "Liyugang(李玉刚)",
            "http://128.199.174.165:8081/photo/LiYuGang-GangHaoYuJianNi-161190597.jpg",
            "http://128.199.174.165:8081/audio/Liyugang(李玉刚)-Ganghaoyujianni-644826494.mp3"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Music Recommendation"
        setContentView(R.layout.activity_music_recommendation)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val intentFilter = IntentFilter(AUDIO_FINISH_PLAY)
        registerReceiver(AudioFinishedReceiver,intentFilter)

        adapterAudio = AudioSongRecycleViewAdapter(this@MusicRecommendation,songs)

        song_rv.apply {
            layoutManager = LinearLayoutManager(this@MusicRecommendation)
            adapter = adapterAudio
        }
    }

    override fun onStart() {
        super.onStart()

        if(IntentService == null){

            IntentService = Intent(this, AudioPlayerService::class.java)
            IntentService?.setAction(ACTION_CREATE)
            startService(IntentService)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
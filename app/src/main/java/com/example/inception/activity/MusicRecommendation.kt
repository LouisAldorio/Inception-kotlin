package com.example.inception.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inception.R
import com.example.inception.adaptor.AudioSongRecycleViewAdapter
import com.example.inception.constant.ACTION_CREATE
import com.example.inception.data.Song
import com.example.inception.service.AudioPlayerService
import kotlinx.android.synthetic.main.activity_music_recommendation.*


var IntentService : Intent? = null
class MusicRecommendation : AppCompatActivity() {


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
        setContentView(R.layout.activity_music_recommendation)

        song_rv.apply {
            layoutManager = LinearLayoutManager(this@MusicRecommendation)
            adapter = AudioSongRecycleViewAdapter(this@MusicRecommendation,songs)
        }
    }

    override fun onStart() {
        super.onStart()

        //media player
        if(IntentService == null){

            IntentService = Intent(this, AudioPlayerService::class.java)
            IntentService?.setAction(ACTION_CREATE)
            startService(IntentService)

        }
    }
}
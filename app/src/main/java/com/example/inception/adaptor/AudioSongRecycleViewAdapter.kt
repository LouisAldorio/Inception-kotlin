package com.example.inception.adaptor

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.activity.IntentService
import com.example.inception.constant.*
import com.example.inception.data.Song
import com.example.inception.objectClass.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.audio_song_layout.view.*

class AudioSongRecycleViewAdapter(val context : Context, val songs: List<Song>) : RecyclerView.Adapter<SongHolder>(){

    val play_button = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRW4zc-4ikB7DFtDEOmp96NqGYJXf6sqZMwEg&usqp=CAU"
    val stop_button = "https://image.flaticon.com/icons/png/512/16/16427.png"

    private val items: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        return SongHolder(LayoutInflater.from(parent.context).inflate(R.layout.audio_song_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int, payloads: MutableList<Any>) {

        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads)
        }else{
            for (payload in payloads) {
                if (payload == AUDIO_RV_PAYLOAD) {

                    Picasso.get().load(items[0]).into(holder.toggleButton)
                    holder.toggleButton.contentDescription = "Stop"
                }
            }
        }

    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        Picasso.get().load(songs[position].cover).into(holder.cover)
        holder.title.text = songs[position].title
        holder.author.text = songs[position].author


        holder.toggleButton.setOnClickListener {
            if (holder.toggleButton.contentDescription == "Stop"){
                IntentService?.setAction(ACTION_STOP)
                context.startService(IntentService)

                IntentService?.setAction(ACTION_PLAY)
                IntentService?.putExtra(CURRENT_PLAYED_SONG,songs[position].url)
                IntentService?.putExtra(CURRENT_PLAYED_SONG_INDEX,position)
                context.startService(IntentService)

                Picasso.get().load(stop_button).into(holder.toggleButton)
                holder.toggleButton.contentDescription = "Start"

                //get previous played and pause the button
                if(User.getpreviousSongIndex(context) != -1) {
                    items.add(play_button)
                    this.notifyItemChanged(User.getpreviousSongIndex(context)!!,
                        AUDIO_RV_PAYLOAD)
                }

                //set State
                User.setPreviousPlayedSong(context,songs[position].url)
                User.setPreviousSongIndex(context,position)

            }else if (holder.toggleButton.contentDescription == "Start"){
                IntentService?.setAction(ACTION_STOP)
                context.startService(IntentService)

                holder.toggleButton.contentDescription = "Stop"
                Picasso.get().load(play_button).into(holder.toggleButton)
            }
        }
    }
}

class SongHolder(view: View) : RecyclerView.ViewHolder(view) {
    var cover : ImageView
    var title : TextView
    var author : TextView
    var toggleButton : ImageButton

    init {
        cover = view.cover
        title = view.title
        author = view.author
        toggleButton = view.toggle_button
    }
}
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

//recycle view adapter akan menerima context dan list dari laguyang akan di render
class AudioSongRecycleViewAdapter(val context : Context, val songs: List<Song>) : RecyclerView.Adapter<SongHolder>(){

    //image untuk play button dan pause button kita ambil saja dari internet
    val play_button = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRW4zc-4ikB7DFtDEOmp96NqGYJXf6sqZMwEg&usqp=CAU"
    val stop_button = "https://image.flaticon.com/icons/png/512/16/16427.png"

    //buat sebuah mutable list untuk menampung data yang akan di transmisikan antar item-item yang ada didalam recycle view
    private val items: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        return SongHolder(LayoutInflater.from(parent.context).inflate(R.layout.audio_song_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int, payloads: MutableList<Any>) {
        // kita akan mengoverride fungsi onBindViewHolder yang menerima payload
        // kita melakukan ini agar , ketika user berganti lagu sebelum lagu yang dipilih sebelumnya selesai diputar, kita dapat mengupdate tampilan
        //dari button yang sedang pause, menjadi button play lagi
        //maka kita check terlebih dahulu, apakah payload kosong
        if(payloads.isEmpty()){
            //jika kosong  maka panggil fungsi onBindViewHolder yang biasa
            super.onBindViewHolder(holder, position, payloads)
        }else{
            //jika payload ada
            for (payload in payloads) {
                //check apakah sinyal payload sama dengan AUDIO_RV_PAYLOAD
                if (payload == AUDIO_RV_PAYLOAD) {
                    // jika sama update view sesuai dengan posisi view holder yang dikirimkan dengan mengambil data yang ditransmisikan melalui mutable list tadi
                    Picasso.get().load(items[0]).into(holder.toggleButton)
                    holder.toggleButton.contentDescription = "Stop"
                }
            }
        }

    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        //pada onBindViewHolder tanpa payload
        //seperti biasa kita bind data kedalam view nya
        Picasso.get().load(songs[position].cover).into(holder.cover)
        holder.title.text = songs[position].title
        holder.author.text = songs[position].author
        Picasso.get().load(play_button).into(holder.toggleButton)


        //disini toggle button adalah tombol untuk memainkan lagu dan menyetop lagu
        holder.toggleButton.setOnClickListener {
            //ketika di click kita akan check apakah lagu sedang dimainkan apa tidak, Stop menandakan lagu yang di click dan ingin diputar tidak sedang dimainkan
            if (holder.toggleButton.contentDescription == "Stop"){
                //kita akan menjalankan aksi stop, untuk menyusuh media player berhenti memainkan lagu yang dipilih/ sebelumnya sedang dimainkan
                IntentService?.setAction(ACTION_STOP)
                context.startService(IntentService)

                //kemudian kita menyuruh media player untuk memainkan lagu yang baru saja dipilih oleh user
                IntentService?.setAction(ACTION_PLAY)
                //tak lupa kita kirimkan data lagu dan index yang diperlukan
                IntentService?.putExtra(CURRENT_PLAYED_SONG,songs[position].url)
                IntentService?.putExtra(CURRENT_PLAYED_SONG_INDEX,position)
                context.startService(IntentService)

                //ubah tampilan button yang semula play menjadi pause, karena lagu sedang dimainkan
                Picasso.get().load(stop_button).into(holder.toggleButton)
                //jangan lupa untuk mengubah contentDescription yang menjadi acuan kita apakah item view tersebut sedang memainkan lagu apa tidak
                holder.toggleButton.contentDescription = "Start"

                //disini kita akan mengecek apakah ada lagu yang sedang dimainkan, sebelum item view saat ini ditekan
                if(User.getpreviousSongIndex(context) != -1) {
                    // jika ada berarti != -1, karena index dari recycle view mulai dari 0 sampai seterusnya
                    items.add(play_button)
                    // kita beritahu fungsi onBindViewHolder satunya yang menerima payload untuk melakukan update view pada item view lagu yang dimainkan sebelumnya
                    this.notifyItemChanged(User.getpreviousSongIndex(context)!!,
                        AUDIO_RV_PAYLOAD)
                }

                //jangan lupa untuk melakuakn set State baru, dimana lagu yang dimainkan saat ini, akan menjadi lagu yang dimainkan previously/ sebelumnya untuk item
                // yang diclick oleh user selanjutnya, kita akan set lagu serta index nya
                User.setPreviousPlayedSong(context,songs[position].url)
                User.setPreviousSongIndex(context,position)

            }else if (holder.toggleButton.contentDescription == "Start"){
                //jika content descrption berupa Start, ini berarti bahwa user melakukan click pada posisi index lagu yang sedang dimainkan
                //maka dari itu kita harus mengambil aksi untuk menghentikan lagu yang sedang dimainkan
                IntentService?.setAction(ACTION_STOP)
                context.startService(IntentService)

                //lalu mengupdate tambila button pada item view manjadi button pay kembali
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
package com.example.inception.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inception.R
import com.example.inception.adaptor.ChatRoomRecycleViewAdapter
import com.example.inception.data.Chat
import com.example.inception.firebase.FirebaseController
import com.example.inception.objectClass.User
import kotlinx.android.synthetic.main.activity_global_chat.*

// activity ini adalah activity yang dijalankan untuk menampilkan global chat room
class GlobalChat : AppCompatActivity() {

    // inisiasi controller yang akan membantu kita untuk berkomunikasi ke firebase DB
    lateinit var controller : FirebaseController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Global Room Chat"
        setContentView(R.layout.activity_global_chat)

        // pada onCreate kita akan menginisialisasi FirebaseCOntroller dan memberikan context dari activity ini
        controller = FirebaseController(this)
        // langsung panggil function listen to chat
        ListenToChat()

        // berikan listener ke button send untuk melakukan send message ke firebase db
        iv_send.setOnClickListener {
            if(et_main.text.toString() != "") {
                SendChat()
            }
        }
    }

    // berikut function lsiten to chat yang akan membantu kita subscribe ke realtime Db dari firebase
    fun ListenToChat() {
        pb_loading.visibility = View.VISIBLE
        // kita panggil fungsi dari readchat yang ada didalam controller
        // serta memberikan sebuah callback function yang kita tuliskan dalam  bentuk lambda expression
         controller.ReadChat { chats ->
             var arrayChats = mutableListOf<Chat>()

             pb_loading.visibility = View.GONE
             // chats adalah map yang nantinya akan dipassing oleh firebase Controller ketika map sudah berhasil disusun
             // tambahkan chat ke dalam array yang dimana nantinya akan kita berikan kepada adpter recycle view kita
             for(item in chats) {
                 arrayChats.add(
                     Chat(
                         item.value.username,
                         item.value.content
                     ))
             }

             // inisisasi adapter
             var ChatAdapter = ChatRoomRecycleViewAdapter(this, arrayChats)
             // masukkan ke recycle view
             rv_main.apply {
                 adapter = ChatAdapter
                 layoutManager = LinearLayoutManager(this@GlobalChat)
                 // jangan lupa untuk selalu mempertahankan agar chat terbaru selalu nampak di paling bawah
                 scrollToPosition(arrayChats.size - 1)
             }
             // clear text view
             et_main.text.clear()
         }
    }

    // fungsi dibawah akan membantu kita untuk mengirimkna data ke firebase DB
    fun SendChat() {
        // disini kita hanya memanggil function yang ada didalam controller dan memberikan parameter berupa object chat data yang ingin kita kirimkan
        controller.AddNewChat(
            Chat(
                User.getUsername(this)!!,
                et_main.text.toString()
            )
        )
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
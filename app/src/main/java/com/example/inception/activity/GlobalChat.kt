package com.example.inception.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inception.R
import com.example.inception.adaptor.ChatRoomRecycleViewAdapter
import com.example.inception.data.Chat
import com.example.inception.firebase.FirebaseController
import com.example.inception.objectClass.User
import kotlinx.android.synthetic.main.activity_global_chat.*

class GlobalChat : AppCompatActivity() {

    lateinit var controller : FirebaseController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Global Room Chat"
        setContentView(R.layout.activity_global_chat)

        controller = FirebaseController(this)
        ListenToChat()

        iv_send.setOnClickListener {
            if(et_main.text.toString() != "") {
                SendChat()
            }
        }
    }

    fun ListenToChat() {
        var chats = controller.ReadChat()
        var arrayChats = mutableListOf<Chat>()

        for(item in chats) {
            arrayChats.add(
                Chat(
                item.value.username,
                item.value.content
            ))
        }
        Log.i("chats",arrayChats.toString())

        var ChatAdapter = ChatRoomRecycleViewAdapter(this, arrayChats)
        rv_main.apply {
            adapter = ChatAdapter
            layoutManager = LinearLayoutManager(this@GlobalChat)
        }
    }

    fun SendChat() {
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
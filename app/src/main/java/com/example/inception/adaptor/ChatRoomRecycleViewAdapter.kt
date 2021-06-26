package com.example.inception.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inception.R
import com.example.inception.data.Chat
import com.example.inception.objectClass.User
import kotlinx.android.synthetic.main.item_chat_receiver.view.*
import kotlinx.android.synthetic.main.item_chat_sender.view.tv_message
import kotlinx.android.synthetic.main.item_chat_sender.view.tv_username

class ChatRoomRecycleViewAdapter(var mContext: Context, private val chats: MutableList<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SENDER -> SenderHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_sender,parent,false))
            else -> ReceiverHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_receiver,parent,false))
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ReceiverHolder -> holder.bindChat(chats[position])
            is SenderHolder -> holder.bindChat(chats[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = chats[position]
        return when {
            item.username == User.getUsername(mContext) -> SENDER
            else -> RECEIVER
        }
    }

    companion object {
        private const val SENDER = 0
        private const val RECEIVER = 1
    }
}

class SenderHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val username = view.tv_username
    private val content = view.tv_message

    fun bindChat(chat: Chat) {
        username.text = chat.username
        content.text = chat.content
    }
}

class ReceiverHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val username = view.tv_receiver_username
    private val content = view.tv_receiver_message

    fun bindChat(chat: Chat) {
        username.text = chat.username
        content.text = chat.content
    }
}
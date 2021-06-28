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


// untuk bagian recycle view adapter nya, disini kita akan memiliki 2 buah view holder
// jika username dari message adalah sama dengan username user yang sedang login , maka kita akan menggunakan sender holder
// dan merender kotak chat di sebelah kanan
// namun jika tidak maka kita akan merender kotak chat di sebelah kiri yang menandakan chat yang dikirim  bukan berasal dari user yang sedang login
class ChatRoomRecycleViewAdapter(var mContext: Context, private val chats: MutableList<Chat>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // pada onCreateViewHolder , kita menerima parameter berupa viewType yang mengembalika nilai int 1(RECEIVER) atau 0(SENDER)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // dari viewType kita dapat menentukan viewHoler mana yang harus kita gunakan
        // jika Type nya adalah sender maka kita akan menginflate SenderHolder beserta xml nya
        // namun jika bukan maka kita akan menginflate ReceiverHolder beserta xml nya yang memiliki layout berbeda
        return when(viewType) {
            SENDER -> SenderHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_sender,parent,false))
            else -> ReceiverHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat_receiver,parent,false))
        }
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    // pada saat proses onBindViewHolder, kita akan menerima parameter holder yang bertipe data RecyclerView.ViewHolder
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // kita akan check apakah holder yang diterima adalah ReceiverHolder atau SenderHolder, lalu invoke fungsi bind yang berbeda dari amsing masing holder
        // sebenarnya tidak perlu di check juga tidak masalah untuk kasus kita, karena nama fungsi pun kita sama kan dan class yang dipassing ke fungsi bind ialah class Chat yang sama

        when(holder) {
            is ReceiverHolder -> holder.bindChat(chats[position])
            is SenderHolder -> holder.bindChat(chats[position])
        }
    }

    // kita juga perlu mengoverride function getItemViewType ynag akan menentukan element list apakah berrasal dari sender atau receiver
    override fun getItemViewType(position: Int): Int {
        val item = chats[position]
        return when {
            item.username == User.getUsername(mContext) -> SENDER
            else -> RECEIVER
        }
    }

    // inisiasi 2 buah variable berupa sender dan receiver yang nanti akan membantu kita menentukan apakah chat tersebut ialah milik user yang sedang login atau bukan
    companion object {
        private const val SENDER = 0
        private const val RECEIVER = 1
    }
}

// kita harus membuat 2 buat class holder, dimana akan memiliki function bind ke view yang berbeda tentunya karena id xml dari view sudah pasti berbeda
class SenderHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val username = view.tv_username
    private val content = view.tv_message

    // disini kita akan bind data chat ke sender
    fun bindChat(chat: Chat) {
        username.text = chat.username
        content.text = chat.content
    }
}

// view class holder untuk receiver
class ReceiverHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val username = view.tv_receiver_username
    private val content = view.tv_receiver_message

    // disini kita akan bind datachat ke receiver
    fun bindChat(chat: Chat) {
        username.text = chat.username
        content.text = chat.content
    }
}
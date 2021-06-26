package com.example.inception.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.inception.data.Chat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseController(context: Context) {
    private var database = Firebase.database
    private val ref = database.getReference("CHATS")
    private  val ctx = context

    fun AddNewChat(chat : Chat) {
        var chatID = ref.push().key.toString()
        ref.child(chatID).setValue(chat).apply {
            addOnCompleteListener {
                Toast.makeText(ctx ,"Chat Sent!", Toast.LENGTH_SHORT).show()
            }
            addOnCanceledListener {  }
            addOnSuccessListener {  }
            addOnFailureListener {
                Toast.makeText(ctx ,"Chat Failed to be Sent!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun ReadChat(callbackUpdateAdapter : (chats : MutableMap<String,Chat>) -> Unit) {
        var allChats = mutableMapOf<String,Chat>()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(ctx ,"Error Fetch Chat!", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot!!.exists()) {
                    for(data in snapshot.children) {
                        var chat = data.getValue(Chat::class.java)
                        var key = data.key.toString()

                        allChats[key] = chat!!
                        callbackUpdateAdapter(allChats)
                    }
                }
            }
        })
    }

    fun deleteChat(key : Set<String>) {
        ref.child(key.first()).removeValue()
    }
}
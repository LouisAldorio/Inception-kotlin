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

//class ini adalah class yang akan membantu kita untuk berkomunikasi dengan firebase realtime database
class FirebaseController(context: Context) {
    // inisiasi instance dari firebase databasenya
    private var database = Firebase.database
    // kita akan memberi nama collection nya berupa CHATS
    private val ref = database.getReference("CHATS")
    // jangan lupa untuk mengambil context yang di passing dari class pemanggil nya
    private  val ctx = context

    // fungsi dibawah merupakan fungsi yang akan mengirimkan data chat ke firebase realtime DB nya
    fun AddNewChat(chat : Chat) {
        // kita akan menyuruh fire saja yang mengenerate unique karna untuk kasus kita, kita tidak akan menggunakan key nya
        var chatID = ref.push().key.toString()
        // fungsi dibawah akan mengirimkan data Chat dengan key yang berhubungan ke firebase realtimr DB
        ref.child(chatID).setValue(chat).apply {
            // disini saya menggunakan onComplete listener dan on Failure listener untuk mengetahui apakah data yang dikirim telah berhasil masuk ke firebase DB atau tidak
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

    // function readchat akan berfungsi untuk melakukan subscribe ke Firebase Realtime Db agar kita dapat mengambil record chat yang ada
    //function ini akan menerima sebuah callback function yang akan bertugas untuk mengupdate adapter dari recycle view agar jika ada chat baru
    // maka recycle view juga ikut ter update
    fun ReadChat(callbackUpdateAdapter : (chats : MutableMap<String,Chat>) -> Unit) {
        // buat sebuah map yang akan menampung data yang dikembalikan dari firebase DB
        var allChats = mutableMapOf<String,Chat>()
        // disini kita akan listen ke data baru yang masuk
        ref.addValueEventListener(object : ValueEventListener {
            // function dibawah kita jalankan jika terjadi kegagalan fetch data dari chat
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(ctx ,"Error Fetch Chat!", Toast.LENGTH_SHORT).show()
            }

            // jiak berhasil fetch data , maka function dibawah yang akan kita jalankan
            // untuk catatan function yang subscibe ke firebase ini , berjalan secara async, maka dari itu memerlukan callback yang akan dijalankan setelah function async berhasil
            //mengembalikan data chat
            override fun onDataChange(snapshot: DataSnapshot) {
                // snapshot akan berisi data baru yang diperoleh
                if(snapshot!!.exists()) {
                    // kita akan loop datanya dan memasukkan kedalam map
                    for(data in snapshot.children) {
                        var chat = data.getValue(Chat::class.java)
                        var key = data.key.toString()

                        // dibawah adalah proses memasukkan kedalam map
                        allChats[key] = chat!!
                        // setelah chat yang baru berhasil di fetch kita akan memanggil callback kita untuk mengupdate adapter
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
package com.example.inception.activity

import android.content.ContentValues
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.Contacts.People
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inception.R
import com.example.inception.adaptor.PhoneContactRecycleViewAdpater
import com.example.inception.data.PhoneContact
import kotlinx.android.synthetic.main.activity_contact_activtity.*


// Pada RTM 6 , di instruksikan untuk menagmbil data lain dari contact selain nama dan nomor telepon
//berikut implementasinya
// disini kami membuat activity yang nantinya digunakan untuk menampilkan data contact
// kami langsung mengimplementasikan LoaderManager dan Callbacknya pada activty
class ContactActivtity : AppCompatActivity() , LoaderManager.LoaderCallbacks<Cursor>{

    //definisikan atribut kolom yang ingin kita ambil datanya
    //disinikan kita akan mengambil contact id , nama nya, kemudian gambar dan email dari contact
    //email dan gambar akan kami ambil dari URI yang berbeda nantinya
    var contactId = ContactsContract.RawContacts.CONTACT_ID
    var displayName = ContactsContract.Contacts.DISPLAY_NAME

    // kolom atribut dibawah digunakan untuk mengambil nama dari contact secara terpisah
    var firstName = ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME
    var lastName = ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
    var middleName = ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME

    var Number = ContactsContract.CommonDataKinds.Phone.NUMBER

    // disini kita buat terlebih dahulu list kosong yang nantinya berguna untuk menampung data dari content provide contact
    var ListContact : MutableList<PhoneContact> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Phone Contacts"

        setContentView(R.layout.activity_contact_activtity)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //pada lifecycle onCreate kita init loader nya agar mulai dijalankan saat activty di buat
        LoaderManager.getInstance(this).initLoader(20123,null,this)
    }

    //ovverride method onCreateLoader , yang dimana digunakan untuk mereturn sebuah cursorlaoder yang digunakan untuk melakukan query ke content provider
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        //definiskan URI Content provider Contact
        var ContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        //Field apa saja yang ingin kita ambil
        var Projection = arrayOf(contactId,displayName,Number)

        // lakkukan query, data akan diurutkan berdasarkan nomor telepon dari yang terkecil ke terbesar
        return CursorLoader(this,ContactUri,Projection,null,null,"${Number} ASC")
    }

    //pada member function onLoadFinished
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        //pastikan isi list kosong
        ListContact.clear()
        //pastikan data yang dikembalikan tidak null
        if(data != null) {
            //pindahkan cursor ke awal
            data.moveToFirst()
            //lakuakn perulangan teru sampai cursor mencapai akhir
            while (!data.isAfterLast){
                //selama perulangan kita akan membaca data, dan memasukkan nya kedalam list
                ListContact.add(
                    //list akan menampung setiap elemen dalam bentuk data class bernama PhoneContact
                    // ambil semua data yang dikembalikan dari cursorLoader
                    //pada kolom email dan photo kita memmanggil method lain yang bertugas mengambil data ke content provider lagi berdasarkan contact id yang telah kita dapatkan
                    PhoneContact(
                        data.getString(data.getColumnIndex(contactId)),
                        data.getString(data.getColumnIndex(displayName)),
                        "",
                        getContactPhoto(data.getString(data.getColumnIndex(contactId))) ?: BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.ic_supplies),
                        geContactEmail(data.getString(data.getColumnIndex(contactId)))!!,
                        data.getString(data.getColumnIndex(Number))
                    )
                )
                //suruh kursor untuk melanjutkan ke baris selanjutnya
                data.moveToNext()
            }

            //setelah semua data berhasil dibaca, masukkan data kedalam recycle view
            contact_recycleview.apply {
                adapter = PhoneContactRecycleViewAdpater(ListContact)
                layoutManager = LinearLayoutManager(this@ContactActivtity)
            }
        }
    }

    //pada member function onLoaderReset instruksikan adapter untuk melakuakn notifyDataSetChanged(), agar jika terjadi perubahan config, atau
    // perubahan data dari sumber , recycle view dapat merespon dan melakukan render ulang terhadap view
    override fun onLoaderReset(loader: Loader<Cursor>) {
        contact_recycleview.adapter?.notifyDataSetChanged()
    }

    //berikut fungsi yang digunakan untuk melakukan query untuk mengambil data email dari content provider
    private fun geContactEmail(contactId: String): String? {
        //deklarasikan variable penampung email
        var email = ""
        //seperti biasa definisikan field yang ingin diambil
        val projection = arrayOf(Email.DATA, Email.TYPE)
        //disini saya menemukan sebuah class yang juga dapat membantu kita melakuakn query ke content provider
        // class ini langsung membalikkan cursor , jadi kita langsung bisa membaca datanya.
        //parameter yang diterima sama dengan CursorLoader sebelumnya
        //tetapi disini kita memberikan sebuah constraints dimana kita menyusuh Loader untuk mengambil data Email
        // berdasarkan contact id yang diberikan
        val data = managedQuery(
            Email.CONTENT_URI,
            projection,
            ContactsContract.Data.CONTACT_ID + "=?",
            arrayOf(contactId),
            null
        )
        // seperti biasanya cek apakah data tidak null
        if (data != null) {
            // instruksikan cursor untuk [indah ke awal
            data.moveToFirst()
            val contactEmailColumnIndex = data.getColumnIndex(Email.DATA)
            //lakukan pembacaan data email selama masih ada yang dapat dibaca
            while (!data.isAfterLast) {
                //masukkan data email yang berhasil dibaca ke dalam variable yang telah di deklarasi sebelumnya
                email = email + data.getString(contactEmailColumnIndex)
                data.moveToNext()
            }
        }
        data.close()
        //kembalikan data emailnya
        return email
    }

    //berikut fungsi yang digunakan untuk mengambil data gambar dari kontak
    private fun getContactPhoto(contactId: String): Bitmap? {
        //deklarasi variabel yang akan menampung bentuk bitmap dari gambar
        var photo: Bitmap? = null
        //definsikan field yang ingin kita ambil berdasarkan contact id yang didapat,
        //dalam kasus ini kita menginginkan photo id
        val projection = arrayOf(ContactsContract.Data.PHOTO_ID)
        // lakukan query untuk mendapatkan photo id berdasarkan contact id yang ada
        val data = managedQuery(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            ContactsContract.Data._ID + "=?",
            arrayOf(contactId),
            null
        )

        //cek apakah data kosong
        if (data != null) {
            //instruksikan cursor untuk pindah ke awal
            data.moveToFirst()
            //ambil data photoId berdasarkan index PHOT_ID
            val photoId = data.getString(data.getColumnIndex(ContactsContract.Data.PHOTO_ID))
            // uuntuk mendapatkan bitmap dari photo id kita membutuhkan sebuah proses lagi
            // disini kami membuat sebuah fungsi langin yang akan mengambil bitmap gambar berdasarkan photo id nya
            photo = photoId?.let { getImageBitmap(it) }
        }
        data.close()
        //return bitmap gambar setelah semua proses selesai dilakukan
        return photo
    }

    //berikut fungsi untuk mendapatkan bitmap gambar berdasarkan photo id
    private fun getImageBitmap(photoId: String): Bitmap? {

        //disini kita akan meminta ke content provider data bitmap yang kita inginkan dengan projection field berupa ContactsContract.CommonDataKinds.Photo.PHOTO
        //field ini akan berisi data bitmap dari file gambar kontak
        //lakukan query dan berikan selection dimana id = photoId yang kita miliki
        val data = managedQuery(
            ContactsContract.Data.CONTENT_URI,
            arrayOf(ContactsContract.CommonDataKinds.Photo.PHOTO),
            ContactsContract.Contacts.Data._ID + "=?",
            arrayOf(photoId),
            null
        )

        //definsikan varible penampung data bitmap nya
        val Bitmap: Bitmap?
        Bitmap = if (data != null) {
            //panggil cursor untuk pindah ke awal
            data.moveToFirst()
            //ambil bentuk byteArray dari file dengan index ContactsContract.CommonDataKinds.Photo.PHOTO)
            val photoBlob = data.getBlob(data.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO))

            // setelah berhasil mendapatkan byte array dari file nya, kita akan malakukan decode byte Array tersebut ke bentuk bitmap sesuai kebutuhan
            BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.size)
        } else {
            null
        }
        data.close()
        //return bitmap yang telah didapat
        return Bitmap
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
package com.example.inception.utils

import android.os.Environment

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import java.io.File
import java.io.FileOutputStream
import java.lang.ref.WeakReference


//disini kita akan membuat kelas yang nanti akan bertugas membantu kita dalam membaca dan menulis file gambar dari internal maupun external storage
//agar dalam proses fetch file gambar tidak menyebabkan freeze pada UI, kita akan menggunakan AsyncTask untuk membantu kita melakukan semuanya dalam bentuk asinkron
//class ini akan menerima paramter berupa path file / lokasi dimana akan disimpan
class DownloadImageAndSaveToStorage(val context: Context,val path : File?) : AsyncTask<String, Unit, String>() {

    //kita akan membuat sebuah context yang kita ambil dari class WeakReference, objek ini nantinya akan kita gunakan
    //untuk menyimpan data didalam internal storage , dan membantu pemrosesan garbage collection secara otomatis
    private var mContext: WeakReference<Context> = WeakReference(context)

    //kita buat terlebih dahulu sebuah fungsi untuk mengenerate sebuah string random yang berisi panjang tertentu
    //dengan kombinasi karakter A-Z , a-z , dan 0-9
    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    //disini kita mengoverride onPostExecute , agar ketika proses simpan ke storage selesai, kita dapa memberi tahu user bahwa file telah berhasil disimpan
    override fun onPostExecute(result: String?) {
        Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
    }


    //didalam function doInBackground kita akan melakukan proses penyimpanan nya
    override fun doInBackground(vararg params: String?) : String{
        //kita ambil terlebih dahulu url dari source file yang ingin kita simpan
        val url = params[0]

        //berikan konfigurasi untuk proses fetch file, disini kita akan menyuruh glie untuk tidak menggunakan mekanismae caching dalam setiap prosesnya
        val requestOptions = RequestOptions()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        //dari objek weakReference yang telah kita buat tddi, lakukan proses penyimpanan
        mContext.get()?.let {
            //agar glide dapat melakukan fetch, kita harus mengubah url kita sebelumnya menjadi tipe data url dari glide
            val urlNew = GlideUrl(
                url, LazyHeaders.Builder()
                    .addHeader("User-Agent", "your-user-agent")
                    .build()
            )

            //setelah sudah menjadi GlideUrl, maka kita akan melemparkan url baru kepada glide dan menyuruh glide untuk melakukan fetch data file, kedalam bentuk bitmap
            val bitmap = Glide.with(it)
                .asBitmap()
                .load(urlNew)
                .apply(requestOptions)
                .submit()
                .get()

            try {
                //disini kita akan menggabungkan path yang kita terima dari constructor , dan membuat sebuah folder image jika tidak ada
                // path akan berisi instance yang nantinya menetukan file akan disimpan pada internal atau external memory
                //jadi mau external maupun internal, kita tetap akan menyimpan file kita didalam folder bermana Images, yang dimana merupakan folder private kita
                //yang hanya boleh diakses melalui aplikasi Inception saja
                var file = File(path, "Images")
                if (!file.exists()) {
                    file.mkdir()

                }
                //disini kita gabungkan lagi path yang sudah ada dengan nama file yang ingin kita tulis bitmapnya
                // kita akan menami file secara random namun unik, dengan panjang hash sebanyak 20 karakter kombinasi
                file = File(file, "${getRandomString(20)}.jpg")

                //fileOutputStream akan membuat aliran keluaran file untuk menulis ke file yang diwakili oleh objek File yang ditentukan.
                val out = FileOutputStream(file)
                //bitmap.compress akan membantu kita menulis data file yang telah di fetch kedalam output stream
                //kita berikan quality = 100 agar kualitas tidak di kompress
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

                //out.Flush akan mengosongkan aliran keluaran ini dan memaksa setiap byte keluaran yang di-buffer untuk dituliskan.
                out.flush()
                //jangan lupa untuk mentuup stream
                out.close()

                //jika semua proses berhasil dijalanka return sebuah string yang nantinya akan kita toast ke user, dimain Thread pada onPost Execute
                return "Image Saved!"

            } catch (e: Exception) {
                //jika gagal, berikan pesan yang jelas pula
                return "Fail to save Image!"
            }
        }
        return ""
    }
}
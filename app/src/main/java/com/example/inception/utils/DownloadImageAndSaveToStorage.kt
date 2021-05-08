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

class DownloadImageAndSaveToStorage(val context: Context,val path : File?) : AsyncTask<String, Unit, String>() {
    private var mContext: WeakReference<Context> = WeakReference(context)

    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    override fun onPostExecute(result: String?) {
        Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
    }


    override fun doInBackground(vararg params: String?) : String{
        val url = params[0]
        val requestOptions = RequestOptions()
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)

        mContext.get()?.let {
            val urlNew = GlideUrl(
                url, LazyHeaders.Builder()
                    .addHeader("User-Agent", "your-user-agent")
                    .build()
            )
            val bitmap = Glide.with(it)
                .asBitmap()
                .load(urlNew)
                .apply(requestOptions)
                .submit()
                .get()

            try {
                var file = File(path, "Images")
                if (!file.exists()) {
                    file.mkdir()

                }
                file = File(file, "${getRandomString(20)}.jpg")

                val out = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)

                out.flush()
                out.close()
                return "Image Saved!"

            } catch (e: Exception) {
                return "Fail to save Image!"
            }
        }
        return ""
    }
}
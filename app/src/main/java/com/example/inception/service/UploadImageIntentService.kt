package com.example.inception.service
import android.content.Intent
import android.content.Context
import androidx.core.app.JobIntentService
import com.example.inception.constant.UPLOAD_JOB_ID
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.inception.constant.ACTION_UPLOAD
import com.example.inception.constant.UPLOADED_FILE_URL
import com.example.inception.constant.UPLOAD_PARAMS
import com.example.inception.data.UploadParams
import com.example.inception.utils.ImageUpload
import java.lang.Exception

//untuk implementasi JobIntentService saya akan menggunakan nya untuk melakukan upload ke server
class UploadImageIntentService : JobIntentService() {
    //inisiasi objeck dari class yang menyediakan fungsi upload didalamnya(upload menggunakan retrofit2)
    val ImageUploader = ImageUpload()
    var url : String = ""
    override fun onHandleWork(intent: Intent) {
        showToast("Start Uploading Image")
        //ambil parameter yang diperlukan untuk membentuk request body agar foto dapat di upload ke server
        val params = intent.getParcelableExtra<UploadParams>(UPLOAD_PARAMS)

        //lakukan upload didalam fungsi onHandleWork(JobIntentService)
        try {
            //upload gambar dan simpan response url ke dalam variabel url yang telah di deklarasi sebelumnya
            url = ImageUploader.upload(params!!.type_adapter,params!!.file)
        } catch (e: Exception) {

        }
    }
    val mHandler: Handler = Handler(Looper.getMainLooper())
    fun showToast(text : String=""){
        mHandler.post(Runnable {
            Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
        })
    }
    //setelah proses upload selesai kita akan melakukan implementasi Broadcast-receiver
    override fun onDestroy() {
        super.onDestroy()
        showToast("Finish Uploading Image")
        //bentuk intent yang nanti akan diidentifikasi oleh receiver
        var intentFileUpload = Intent(ACTION_UPLOAD)
        //masukkan data yang telah didapat dari server ke dalam extra intent nya agar receiver dapat mengambil data yang di pass dari service nantinya
        intentFileUpload.putExtra(UPLOADED_FILE_URL,url)
        //lakukan broadcast dengan membawa data URL image nya
        sendBroadcast(intentFileUpload)
    }
    companion object{
        //fungsi static yang akan dipanggil untuk memulai service
        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context, UploadImageIntentService::class.java, UPLOAD_JOB_ID,intent)
        }
    }
}
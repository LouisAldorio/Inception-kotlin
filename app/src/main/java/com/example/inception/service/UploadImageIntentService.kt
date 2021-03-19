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

class UploadImageIntentService : JobIntentService() {
    val ImageUploader = ImageUpload()
    var url : String = ""
    override fun onHandleWork(intent: Intent) {
        showToast("Start Uploading Image")
        val params = intent.getParcelableExtra<UploadParams>(UPLOAD_PARAMS)

        try {
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

    override fun onDestroy() {
        super.onDestroy()
        showToast("Finish Uploading Image")
        var intentFileUpload = Intent(ACTION_UPLOAD)
        intentFileUpload.putExtra(UPLOADED_FILE_URL,url)
        sendBroadcast(intentFileUpload)
        showToast(url)
    }


    companion object{
        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context, UploadImageIntentService::class.java, UPLOAD_JOB_ID,intent)
        }
    }
}
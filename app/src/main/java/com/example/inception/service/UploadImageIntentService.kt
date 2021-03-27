package com.example.inception.service
import android.content.Intent
import android.content.Context
import androidx.core.app.JobIntentService
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.inception.constant.*
import com.example.inception.data.UploadParams
import com.example.inception.utils.ImageUpload
import java.lang.Exception

class UploadImageIntentService : JobIntentService() {
    val ImageUploader = ImageUpload()
    var url : String = ""
    var uploadContext = ""
    override fun onHandleWork(intent: Intent) {
        showToast("Start Uploading Image")
        val params = intent.getParcelableExtra<UploadParams>(UPLOAD_PARAMS)
        uploadContext = intent.getStringExtra(UPLOAD_CONTEXT)!!

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
        var action = ACTION_UPLOAD_PROFILE

        if (uploadContext == "create_commodity_attachment"){
            action = ACTION_UPLOAD_ATTACHMENT
        }

        var intentFileUpload = Intent(action)
        intentFileUpload.putExtra(UPLOADED_FILE_URL,url)
        intentFileUpload.putExtra(UPLOAD_CONTEXT,uploadContext)
        sendBroadcast(intentFileUpload)
    }

    companion object{
        fun enqueueWork(context: Context, intent: Intent){
            enqueueWork(context, UploadImageIntentService::class.java, UPLOAD_JOB_ID,intent)
        }
    }
}
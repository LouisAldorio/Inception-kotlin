package com.example.inception.jobScheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import com.example.inception.constant.COVID_NEW_UPDATE
import com.example.inception.constant.COVID_NEW_UPDATE_DATA
import com.example.inception.constant.UPLOADED_FILE_URL
import com.example.inception.constant.UPLOAD_CONTEXT
import com.example.inception.data.CovidStatistic
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

// Berhubung masa pandemic , ktia akan membuat jobScheduler yang bertugas mengambil data pasien Covid-19
// Jadi aplikasi dapat membantu user memantau situasi terkini saat pandemik
class CovidJobScheduler : JobService() {

    //disini kita mendefinisikan variable yang diperlukan untuk melakukan query ke API Covid
    val country = "Indonesia"
    val API_KEY = "1ac87e87ccmsha418197d791ff87p1c1beajsnbb544ed0e2f3"
    val host = "covid-19-data.p.rapidapi.com"

    //on StartJob akan saya return true dimana meskip[un berhasil , tetap reschedule dengan tujuan agar data yang didapat ialah data terbaru setiap saat
    override fun onStartJob(params: JobParameters?): Boolean {
        //pada saat job di mulai panggil fungsi yang bertugas melakuakn request ke server
        GetCovidData(params)
        return true
    }

    //saat gagal kita juga akan reschedule, sesuai dengan kondisi yang telah kita tentukan
    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    //kita akan membuat function yang berfungsi melakukan requets ke server
    private fun GetCovidData(params: JobParameters?) {
        //definisikan client untuk melakukan requets secara async
        var client = AsyncHttpClient()
        //definisikan end points
        var url = "https://covid-19-data.p.rapidapi.com/country?name=$country"
        // karena response body yang diterima akan berupa byte array , harus kita konversi ke string dengan standar UTF-8
        val charset = Charsets.UTF_8

        //definisikan handler yang akan menangani response yang didapat dari server
        var handler = object : AsyncHttpResponseHandler() {

            //jika response berhasil didapat makan terjemahkan object json kedalam object parcelable agar nanti dapat kita parce kedalam intent yang akan
            // digunakan dalam broadcast
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                var result = responseBody?.toString(charset) ?: "Covid-19 is GONE!"
                Log.i("TAG",result)
                var jsonArray = JSONArray(result)
                var objectData = jsonArray.getJSONObject(0)

                //definisikan intent dengan konteks COVID_NEW_UPDATE
                var intentCovidUpdate = Intent(COVID_NEW_UPDATE)
                //isi data yang didapat dari server ke dalam intent extra
                intentCovidUpdate.putExtra(COVID_NEW_UPDATE_DATA,CovidStatistic(
                    objectData.getString("country"),
                    objectData.getString("code"),
                    objectData.getString("confirmed"),
                    objectData.getString("recovered"),
                    objectData.getString("critical"),
                    objectData.getString("deaths"),
                    objectData.getString("latitude"),
                    objectData.getString("longitude"),
                    objectData.getString("lastChange"),
                    objectData.getString("lastUpdate")
                ))
                //kirimkan broadcast yang membawa data yang akan di render pada activity
                sendBroadcast(intentCovidUpdate)
                //tetap lakukan reschedule untuk mendapat data terbaru
                jobFinished(params,true)
            }

            //jika terjadi ke gagalan jadwalkan ulang untuk mengambil data
            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                jobFinished(params,true)
            }
        }
        //masukkan API KEY yang dibutuhkan kedalam header dari request
        client.addHeader("x-rapidapi-key", API_KEY)
        //masukkan juga host kedalam header sesuai dengan ketentuan yang diberlakuakn oleh penyedia API
        client.addHeader("x-rapidapi-host",host)
        //lakukan request serta berikan handler yang sebelumnya telah dibuat, agar response dapat di tangani
        client.get(url,handler)
    }


}
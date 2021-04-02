package com.example.inception.jobScheduler

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import java.text.SimpleDateFormat
import java.util.*

class CovidJobScheduler : JobService() {

    val country = "Indonesia"
    val API_KEY = "1ac87e87ccmsha418197d791ff87p1c1beajsnbb544ed0e2f3"
    val host = "covid-19-data.p.rapidapi.com"

    override fun onStartJob(params: JobParameters?): Boolean {
        GetCovidData(params)
        return false
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return true
    }

    private fun GetCovidData(params: JobParameters?) {
        var client = AsyncHttpClient()
        var url = "https://covid-19-data.p.rapidapi.com/country?name=$country"
        val charset = Charsets.UTF_8

        var handler = object : AsyncHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                var result = responseBody?.toString(charset) ?: "Covid-19 is GONE!"
                Log.w("TAG",result)
                jobFinished(params,false)
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                jobFinished(params,true)
                Log.w("TAG", "Failed")
            }
        }
        client.addHeader("x-rapidapi-key", API_KEY)
        client.addHeader("x-rapidapi-host",host)
        client.get(url,handler)
    }


}
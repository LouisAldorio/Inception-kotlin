package com.example.inception.activity

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.inception.R
import com.example.inception.constant.COVID_NEW_UPDATE
import com.example.inception.constant.COVID_NEW_UPDATE_DATA
import com.example.inception.data.CovidStatistic
import com.example.inception.jobScheduler.CovidJobScheduler
import com.example.inception.objectClass.User
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.android.synthetic.main.activity_covid.*
import java.text.SimpleDateFormat
import java.util.*

//pada activity yang bertugas merender data response dari server
class CovidActivity : AppCompatActivity() {
    //definisikan job ID
    val JobId = 15000

    private var mInterAds : InterstitialAd? = null
    //buat sebuah receiver yang akan menerima broadcast dari jobScheduler ketika respons editerima
    private val NewCovidUpdateReceiver = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            //broadcast yang sebelumnya dikirimkan membawa payload data berupa parcelable
            //ambil data nya dan masukkan kedalam view XML yang telah kita buat
            val data = intent?.getParcelableExtra<CovidStatistic>(COVID_NEW_UPDATE_DATA)
            deaths.text = data?.deaths
            confirmed.text = data?.confirmed
            recovered.text = data?.recovered
            country.text = data?.country
            critical.text = data?.critical

            val sdf = SimpleDateFormat("hh:mm:ss, dd MMM yyyy")
            val currentDate = sdf.format(Date())
            last_updated.text = currentDate
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Covid-19 Report"
        setContentView(R.layout.activity_covid)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        //pada saat actvity di create kita akan daftarkan receiver yang akan menangkap broadcast dari jobScheduler
        val intentFilter = IntentFilter(COVID_NEW_UPDATE)
        registerReceiver(NewCovidUpdateReceiver,intentFilter)
        //mulai job untuk melakukan request,dan reschedulling untuk terus mengambil data terbaru
        startMyJob()

        if (User.getSubscription(this) == 0) {
            //Load interstitial adds
            InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712",
                AdRequest.Builder().build(), object : InterstitialAdLoadCallback(){

                    override fun onAdFailedToLoad(p0: LoadAdError) {
                        Toast.makeText(this@CovidActivity, "Ads Load Failed",
                            Toast.LENGTH_SHORT).show()
                        mInterAds = null
                    }

                    override fun onAdLoaded(p0: InterstitialAd) {
                        super.onAdLoaded(p0)
                        mInterAds = p0
                    }
                })
        }

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

    override fun onDestroy() {
        super.onDestroy()
        //cancel job saat onDestroy untuk menghemat resource
        cancelMyJob()
    }

    override fun onResume() {
        super.onResume()
        if (mInterAds != null) {
            mInterAds!!.show(this)
        }
    }


    //definisikan function untuk memulai jobScheduler
    private fun startMyJob() {
        //buat variabel untuk menampung balikan dari class ComponentName yang kita buat dengan memberikan sebuah jobScheduler berupa CovidJobScheduler
        var serviceComponent = ComponentName(this, CovidJobScheduler::class.java)
        //buat variabel object jobInfo yang akan bertugas menampung semua Constraint yang kita definisikan untuk menjalakan job Scheduler
        //jangan lupa pula berikan job ID
        val JobInfo = JobInfo.Builder(JobId, serviceComponent)
                //kita dapat memulai scheduler dengan tipe koneksi internet apapun
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                //set delay sebelum tiap job dijalankan ulang ialah 3 menit
                // saya tidak menggunakan setPeriodic dikarenakan batas minimum ialah 15 menit
//            .setMinimumLatency(3 * 60 * 1000)

        //definisikan jobScheduler Service nya
        var JobCovid = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        //dan jalankan
        JobCovid.schedule(JobInfo.build())
    }

    //definisikan fungsi untuk memvbatalkan scheduler
    private fun cancelMyJob() {
        var jobCovid = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        //batakan dengan memberukan job ID bersangkutan yang ingin dibatalkan
        jobCovid.cancel(JobId)
    }
}
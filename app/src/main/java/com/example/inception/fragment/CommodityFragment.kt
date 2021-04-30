package com.example.inception.fragment

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.example.inception.GetCommodityQuery
//import com.example.inception.GetSupplierQuery
import com.example.inception.R
import com.example.inception.adaptor.AllCategorizedCommodityRecycleViewAdapter
import com.example.inception.api.apolloClient
import com.example.inception.constant.ALARM_MANAGER_CHANNELID
import com.example.inception.constant.EXTRA_PESAN
import com.example.inception.internalreceiver.ScheduledAlarmReceiver
import com.example.inception.utils.Capitalizer
import com.example.inception.utils.EspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class CommodityFragment : Fragment() {

    lateinit var mainCategoryRecycler: RecyclerView
    var mainRecyclerAdapter: AllCategorizedCommodityRecycleViewAdapter? = null
    lateinit var objectView: View

    //membuat alarm manager sebagai daily reminder kepada user untuk jangan lupa mengecek aplikasi
    //membuat variable yang di perlukan serta alarm manager
    private var pendingIntent: PendingIntent? = null
    private var startIntent: Intent? = null
    private var alarmManager: AlarmManager? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //definisikan alarm manager dan isi kedalam variable yang telah dibuat sebelumnya
        alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //agar menghemat resource cek terlebih dahulu apakah pending intent sudah ada, jika ada cancel semua pending intent yang ada agar menghemat resource
        if(pendingIntent != null){
            alarmManager?.cancel(pendingIntent)
            pendingIntent?.cancel()
        }

        //definiskan waktu deplay pertama kali sebelum job dijalankan
        var timeToWait = Calendar.getInstance()
        //disini saya akan menunggu selama 5 detik sebelum job pertama dijalankan
        timeToWait.add(Calendar.SECOND,5)
        //buat intent dengan tujuan sebauh receiver yang nantinya akan berisi sekumpulan instruksi yang akan dijalankan oleh alarm Manager
        startIntent = Intent(activity, ScheduledAlarmReceiver::class.java)

        //berikan data yang diperlukan pada saat job di jalankan
        startIntent?.putExtra(ALARM_MANAGER_CHANNELID,"Daily reminder")
        startIntent?.putExtra(EXTRA_PESAN,"Don't forget to check out more Commodity!")

        //kita menggunakan pending intetn dengan tujuan untuk menunda broadcast selama waktu yang ditentukan
        pendingIntent = PendingIntent.getBroadcast(activity,100,startIntent,0)
        //disini kita menyuruh alarm manager untuk menjadwalkan reminder setiap setengah hari / 12 jam untuk mengingatkan user agar selalu mengecek update terbaru yang ada
        //didalam aplikasi
        alarmManager?.setInexactRepeating(AlarmManager.RTC,timeToWait.timeInMillis, AlarmManager.INTERVAL_HALF_DAY,pendingIntent)

        objectView = inflater.inflate(R.layout.fragment_commodity, container, false)
        return objectView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //untuk setiap proses yang melakukan request ke API, sebelum dilakukn requets, kita mulai proses idling
        EspressoIdlingResource.increment()
        lifecycleScope.launch(Dispatchers.Main) {
            val response = try {
                withContext(Dispatchers.IO) {
                    apolloClient(requireContext()).query(GetCommodityQuery(page = 1, limit = 10))
                        .await()
                }
            } catch (e: ApolloException) {
                Log.d("CommodityList", "Failure", e)
                null
            }
            this@CommodityFragment.view?.let { arrange(it, response) }
            //kita akhiri psoes Idling setelah data selesai di set kedalam view agar, apda Unit test nanti, dapat dilanjutkan ke proses test berikutnya
            EspressoIdlingResource.decrement()
        }
    }

    private fun arrange(view: View, response: Response<GetCommodityQuery.Data>?) {
        var allCategoryList: List<GetCommodityQuery.Comodities_with_category> = ArrayList()
        val commodities = response?.data?.comodities_with_categories
        if (commodities != null && !response.hasErrors()) {
            view.findViewById<ProgressBar>(R.id.commodity_progress_bar).visibility = View.GONE
            allCategoryList = (commodities as List<GetCommodityQuery.Comodities_with_category>?)!!
            setMainCategoryRecycler(allCategoryList)
        }
    }

    private fun setMainCategoryRecycler(allCategoryList: List<GetCommodityQuery.Comodities_with_category >) {
        mainCategoryRecycler = objectView.findViewById(R.id.rvMain)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        mainCategoryRecycler.setLayoutManager(layoutManager)
        mainRecyclerAdapter = AllCategorizedCommodityRecycleViewAdapter(requireActivity(), allCategoryList)
        mainCategoryRecycler.setAdapter(mainRecyclerAdapter)
    }

}
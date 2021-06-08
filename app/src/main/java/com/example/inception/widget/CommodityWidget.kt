package com.example.inception.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.widget.RemoteViews
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.inception.GetCommodityForWidgetPaginationQuery
import com.example.inception.R
import com.example.inception.activity.DetailPage
import com.example.inception.activity.LandingPage
import com.example.inception.api.apolloClient
import com.example.inception.constant.CONTEXT_EXTRA
import com.example.inception.constant.DETAIL_EXTRA
import com.example.inception.data.Commodity
import com.example.inception.data.CommodityUser
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.doAsync


// disini kita membuat sebuat kelas , bernama CommodityWidget yang merupakan turunan dari widget provider
class CommodityWidget : AppWidgetProvider() {

    //fungsi onUpdate akan melakukan Update terhadap widget baik data maupun UI
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // kita menggunakan loop karena , user dapat menggunakan lebih dari 1 wigget untuk 1 jenis widget yang sama, jadi update nya harus sinkron
        for (appWidgetId in appWidgetIds) {
            // fungsi dibawah merupakan fungsi internal yang akan kita gunakan untuk menulis kode yang akan mengupdate widget
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    // kita tidak akan melakukan apapun ketika widget pertama kali dibuat
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    // kita juga tidak akan melakukan apapun ketika widget dihancurkan
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // disini kita membuat sebuah receiver yang akan menerima broadcast ketika tombol dari widget ditekan
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        // ketika sinyal broadcast diterima maka kita akan memangggil widget manager
        val appWidgetManager = AppWidgetManager.getInstance(context)
        // kita memerlukan widget manager karena kita tidak tau ada berapa widget id / widget yang sedang berjalan
        val thisAppWidgetComponenName = ComponentName(context!!.packageName, javaClass.name)

        // ambil widget ids yang diketahui oleh widget manager
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponenName)
        // update semua widget yang id nya terdaftar pada widget manager
        for(appWidgetId in appWidgetIds){
            updateAppWidget(context, appWidgetManager,appWidgetId)
        }
    }

    // fungsi dibawah merupakan fungsi private yang kita buat manual yang berguna untuk melakukan request ke Graphql API untuk mengambil data commodity
    private fun RequestCommodityData(context: Context, views: RemoteViews,appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        // untuk membuat widget lebih interaktif kita menyediakan sebuah progress bar/ loading
        // dimana progress bar ini akan dimunculkan ketika user menekan tombol request ke API
        // kita juga akan menghilangkan layout utama yang menampung data
        views.setViewVisibility(R.id.widget_commodity_item, View.GONE)
        views.setViewVisibility(R.id.widget_progress_bar, View.VISIBLE)

        //definisikan array kosong yang akan menmapung data yang diterimak dari API
        val CommodityList: MutableList<Commodity> = arrayListOf()

        // disini agat UI tidak freeze/ tetap smooth, kita akan menggunakan thread berbeda dengan bantuan Coroutine
        GlobalScope.launch {
            // pada bagian ini kita akan mencoba melakukan request ke APi
            val response = try {
                // kita akan menggunakan thread IO
                withContext(Dispatchers.IO) {
                    // menggunakan graphql Client untuk melakukan request dengan memeberikan schema yang ingin di fetch
                    // serta berikan page dan limit yang merupakan parameter dari query, disini kita membatasi limit hanya 50 , dikarenakan data yang dibalikkan akan selalu
                    // 50 data terbaru yang dibuat oleh pengguna
                    apolloClient(context).query(
                        GetCommodityForWidgetPaginationQuery(
                            page = 1,
                            limit = 50
                        )
                    ).await()
                }
            } catch (e: ApolloException) {
                // jika error kita cactch
                Log.d("Commodity_List", "Failure", e)
                null
            }

            //setelah data diterima , kita akan melakukan loop terhadap data balikan kita, lalu memasukkan kedalam array yang telah ktia definisikan sebelumnya
            // disini yang kita butuhkan untuk ditampilkan hanyalah nama gambar serta harga
            for (item in response!!.data!!.comodities.nodes) {
                CommodityList.add(
                    Commodity(
                        item!!.name,
                        item!!.image, item!!.unit_price.toInt().toString(), "", "",
                        CommodityUser("", "", "", ""), ""
                    )
                )
            }

            // kita akan merandom index dari 50 data yang diterima
            var index = (1..CommodityList.size).random()

            // set data kedalam view melalui RemoteView, data berupa nama dan harga, kedalam textView
            views.setTextViewText(R.id.widget_commodity_name,CommodityList[index - 1].name)
            views.setTextViewText(R.id.widget_commodity_price,"Rp. " + CommodityList[index - 1].unit_price)

            // untuk gambar kita akan memanfaat kan Glide
            val url = GlideUrl(
                CommodityList[index - 1].image[0], LazyHeaders.Builder()
                    .addHeader("User-Agent", WebSettings.getDefaultUserAgent(context))
                    .build()
            )

            //glide akan membantu kita mengubah gambar dari URL ke dalam bentuk Bitmap yang nanti dapat kita masukan ke dalam bitmap dari ImageView
            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit(150, 150).get()
            //set bitmap kedalam image view
            views.setImageViewBitmap(R.id.widget_commodity, bitmap)

            // buat sebuah intent yang akan dijalankan ketikan user menekan item layout data data commodity
            var intentdetail = Intent(context,DetailPage::class.java)
            intentdetail.putExtra(DETAIL_EXTRA,CommodityList[index - 1])
            intentdetail.putExtra(CONTEXT_EXTRA,"Commodity")

            // berikan click listener kepada view yang akan menredirect ktia ke halaman detail ketika widget commodity item ditekan
            views.setOnClickPendingIntent(R.id.widget_commodity_item,
                PendingIntent.getActivity(context, 1891, intentdetail ,PendingIntent.FLAG_UPDATE_CURRENT))

            // munculkan item view nya dan hilangkan progress bar
            views.setViewVisibility(R.id.widget_commodity_item, View.VISIBLE)
            views.setViewVisibility(R.id.widget_progress_bar, View.GONE)

            // suruh widget manager untuk melakukan update terhadap view dari widget dan beripakan paramter dan widget id yang telah kita set sebelumnya
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    // fungsi ini akan dijalankan untuk mengupdate widget
    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // definisikan kelas remoteView yang akan membantu kita menghubungkan data kedalam view
        val views = RemoteViews(context.packageName, R.layout.commodity_widget)

        // panggil fungsi async yang akan melakukan request ke API
        RequestCommodityData(context,views,appWidgetManager,appWidgetId)

        // sembari melakukan request kita juga perlu set icon dari button refresh
        views.setImageViewBitmap(R.id.widget_refresh, BitmapFactory.decodeResource(context.getResources(),
            R.drawable.reload))

        // berikan listener kedalam buttonrefresh yang dimana jika ditekan akan melakukan broadcast, ke receiver di kelas ini juga
        views.setOnClickPendingIntent(R.id.widget_refresh,
            Intent(context,
                CommodityWidget::class.java).let {
                PendingIntent.getBroadcast(context,653,it,PendingIntent.FLAG_UPDATE_CURRENT)
            })

        // jangan lupa pula apabila layout dari widget di click, redirect user kedalam halaman landing page commodity
        views.setOnClickPendingIntent(R.id.widget_layout,
            PendingIntent.getActivity(context, 987, Intent(context,LandingPage::class.java) ,0))

        // suruh widget manager untuk melakukan update dari widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}


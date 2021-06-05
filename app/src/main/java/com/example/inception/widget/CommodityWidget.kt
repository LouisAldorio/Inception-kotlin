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
import android.widget.RemoteViews
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.inception.GetCommodityForWidgetPaginationQuery
import com.example.inception.R
import com.example.inception.api.apolloClient
import com.example.inception.data.Commodity
import com.example.inception.data.CommodityUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * Implementation of App Widget functionality.
 */
class CommodityWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        var widgetpref = WidgetPref(context!!)
        if(intent?.action.equals("refresh")) {
            widgetpref.page++
        }
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidgetComponenName = ComponentName(context!!.packageName, javaClass.name)

        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponenName)
        for(appWidgetId in appWidgetIds){
            updateAppWidget(context, appWidgetManager,appWidgetId)
        }
    }

    companion object {
        fun RequestCommodityData(context: Context, views: RemoteViews) {

            var widgetSharePref = WidgetPref(context)
            val CommodityList: MutableList<Commodity> = arrayListOf()


            GlobalScope.launch {
                val response = try {
                    withContext(Dispatchers.IO) {
                        apolloClient(context).query(
                            GetCommodityForWidgetPaginationQuery(
                                page = widgetSharePref.page,
                                limit = 10
                            )
                        ).await()
                    }
                } catch (e: ApolloException) {
                    Log.d("Commodity_List", "Failure", e)
                    null
                }

                Log.i("response_api",response.toString())

                for (item in response!!.data!!.comodities.nodes) {
                    CommodityList.add(
                        Commodity(
                            item!!.name,
                            item!!.image, "", "", "",
                            CommodityUser("", "", "", ""), ""
                        )
                    )
                }
                var index = (0..CommodityList.size).random()
                views.setTextViewText(R.id.commodity_name,CommodityList[index - 1].name)
                try {
                    val url = GlideUrl(
                        CommodityList[index - 1].image[0], LazyHeaders.Builder()
                            .addHeader("User-Agent", "your-user-agent")
                            .build()
                    )
                    val bitmap: Bitmap = Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .submit(200, 200)
                        .get()
                    views.setImageViewBitmap(R.id.widget_commodity, bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.commodity_widget)

            RequestCommodityData(context,views)
            views.setImageViewBitmap(R.id.widget_refresh, BitmapFactory.decodeResource(context.getResources(),
                R.drawable.trash))
            views.setOnClickPendingIntent(R.id.widget_refresh,
                getPendingSelfIntent(context, "refresh"))

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }


        private fun getPendingSelfIntent(context: Context, action: String): PendingIntent? = Intent(context,
            CommodityWidget::class.java).let {
            it.action = action
            PendingIntent.getBroadcast(context,653,it,PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}


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
import com.example.inception.activity.LandingPage
import com.example.inception.api.apolloClient
import com.example.inception.data.Commodity
import com.example.inception.data.CommodityUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.doAsync


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

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidgetComponenName = ComponentName(context!!.packageName, javaClass.name)

        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponenName)
        for(appWidgetId in appWidgetIds){
            updateAppWidget(context, appWidgetManager,appWidgetId)
        }
    }

    private fun RequestCommodityData(context: Context, views: RemoteViews,appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        views.setViewVisibility(R.id.widget_commodity_item, View.GONE)
        views.setViewVisibility(R.id.widget_progress_bar, View.VISIBLE)

        val CommodityList: MutableList<Commodity> = arrayListOf()

        GlobalScope.launch {
            val response = try {
                withContext(Dispatchers.IO) {
                    apolloClient(context).query(
                        GetCommodityForWidgetPaginationQuery(
                            page = 1,
                            limit = 50
                        )
                    ).await()
                }
            } catch (e: ApolloException) {
                Log.d("Commodity_List", "Failure", e)
                null
            }

            for (item in response!!.data!!.comodities.nodes) {
                CommodityList.add(
                    Commodity(
                        item!!.name,
                        item!!.image, item!!.unit_price.toInt().toString(), "", "",
                        CommodityUser("", "", "", ""), ""
                    )
                )
            }

            var index = (1..CommodityList.size).random()

            views.setTextViewText(R.id.widget_commodity_name,CommodityList[index - 1].name)
            views.setTextViewText(R.id.widget_commodity_price,"Rp. " + CommodityList[index - 1].unit_price)

            val url = GlideUrl(
                CommodityList[index - 1].image[0], LazyHeaders.Builder()
                    .addHeader("User-Agent", WebSettings.getDefaultUserAgent(context))
                    .build()
            )

            val bitmap: Bitmap = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit(150, 150)
                .get()
            views.setImageViewBitmap(R.id.widget_commodity, bitmap)

            views.setViewVisibility(R.id.widget_commodity_item, View.VISIBLE)
            views.setViewVisibility(R.id.widget_progress_bar, View.GONE)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.commodity_widget)

        RequestCommodityData(context,views,appWidgetManager,appWidgetId)

        views.setImageViewBitmap(R.id.widget_refresh, BitmapFactory.decodeResource(context.getResources(),
            R.drawable.reload))

        views.setOnClickPendingIntent(R.id.widget_refresh,
            Intent(context,
                CommodityWidget::class.java).let {
                PendingIntent.getBroadcast(context,653,it,PendingIntent.FLAG_UPDATE_CURRENT)
            })

        views.setOnClickPendingIntent(R.id.widget_layout,
            PendingIntent.getActivity(context, 987, Intent(context,LandingPage::class.java) ,0))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}


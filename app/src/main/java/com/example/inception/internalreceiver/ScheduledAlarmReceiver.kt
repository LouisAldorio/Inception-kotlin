package com.example.inception.internalreceiver

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.inception.R
import com.example.inception.activity.DetailPage
import com.example.inception.activity.LandingPage
import com.example.inception.constant.*
import com.example.inception.data.Commodity
import com.example.inception.data.CommodityUser

class ScheduledAlarmReceiver : BroadcastReceiver() {

    //pada receiver , karena kita akan menggunakan notification channel maka berikan annotasi yang membatasi pembuatan channel jika API < android O
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        //pada receiver definisikan id notifikasi, id channel nama dan tingkat kepentingan nya
        val NotificationId = 20000
        val channelId = intent.getStringExtra(ALARM_MANAGER_CHANNELID)
        val name = intent.getStringExtra(ALARM_MANAGER_CHANNELID)
        val importance = NotificationManager.IMPORTANCE_HIGH

        // disini kita akan membuat channel group baru bernama App Activity
        //sehingga aplikasi kita akan memiliki 2 channel group berupa Activity(Acitivity dari user)
        // dan App Activity, yang dimana notifikasi akan dijalankan berdasarkan aktivitas aplikasi
        val list = mutableListOf<NotificationChannelGroup>()
        list.add(
            NotificationChannelGroup(
                "App Activity",
                "App Activity"
            )
        )

        //buat channel dan masukkan channel yang dibuat kedalam Channle Group App Activity
        val NotifyChannel = NotificationChannel(channelId, name, importance)
        NotifyChannel.group = "App Activity"

        // kita akan mneggunakan custom layout, dan memberikan informasi yang diperlukan untuk nantinya ditampilkan pada notifikasi
        val contentView = RemoteViews(context.packageName, R.layout.custom_notification_layout)
        contentView.setTextViewText(R.id.sub_title,"Don't miss it!")
        contentView.setTextViewText(R.id.notif_title,intent?.getStringExtra(EXTRA_PESAN))

        // buat pending intent yang akan membuka activity tertentu apabila notifikasi di tekan
        val resultIntent = Intent(context, LandingPage::class.java)
        resultIntent.putExtra(CONTEXT_EXTRA,"Commodity")
        resultIntent.putExtra(NOTIFICATION_CONTEXT,true)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_CANCEL_CURRENT)
        }

        // bangun notification dengan builder dan masukkan layout yang sudah diinisiasi di atas serta pending intent nya,
        val Builder = NotificationCompat.Builder(context!!,channelId!!)
            .setSmallIcon(R.drawable.ic_commodity)
            .setContent(contentView)
            .setContentIntent(resultPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //kita akan menghilangkan notifikasi setelah user menekan notifikasi
            .setAutoCancel(true)

        //inisiasi notification manager
        var NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //buat channel group
        NotificationManager.createNotificationChannelGroups(list)
        //buat notification channel
        NotificationManager.createNotificationChannel(NotifyChannel)
        //jalankan notifikasi
        NotificationManager.notify(NotificationId,Builder.build())
    }
}
package org.techtown.accountbook.Service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import org.techtown.accountbook.R
import org.techtown.accountbook.UI.activity.MainActivity
import org.techtown.accountbook.util.BankFinder
import timber.log.Timber
import kotlin.Exception

class MyNotificationListenerService : NotificationListenerService() {

    val channelId = "channelId"
    val channelName = "Foreground Service Channel"
    var notificationId = 1
    val DEBOUNCE_TIME_MS = 2000
    var lastNotificationTime = 0

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val currentTime = System.currentTimeMillis()

        if(currentTime - lastNotificationTime < DEBOUNCE_TIME_MS ) return

        val packageName: String = sbn?.packageName ?: "Null"
        val extras = sbn?.notification?.extras

        val extraTitle: String = extras?.get(Notification.EXTRA_TITLE).toString()
        val extraText: String = extras?.get(Notification.EXTRA_TEXT).toString()
        val extraBigText: String = extras?.get(Notification.EXTRA_BIG_TEXT).toString()
        val extraInfoText: String = extras?.get(Notification.EXTRA_INFO_TEXT).toString()
        val extraSubText: String = extras?.get(Notification.EXTRA_SUB_TEXT).toString()
        val extraSummaryText: String = extras?.get(Notification.EXTRA_SUMMARY_TEXT).toString()

        if(extraTitle=="null" && extraText=="null") return
        Timber.d("onNotificationPosted:" + "\n" + "PackageName: " + packageName + "\nTitle: " + extraTitle + "\n" + "Text: " + extraText + "\n" + "BigText: " + extraBigText + "\n" + "InfoText: " + extraInfoText + "\n" + "SubText: " + extraSubText + "\n" + "SummaryText: " + extraSummaryText + "\n")

        if(BankFinder.isAppending(extraTitle)){
            var data = BankFinder.checkMoney(extraTitle)
            if(data != "null"){
                data = data.dropLast(1)
                notificationId++
                val notiIntent = Intent(applicationContext, MainActivity::class.java)
                Timber.e("money = $data")
                notiIntent.putExtra("money",data)
                notiIntent.putExtra("addSpending",true)
                notiIntent.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
                Timber.e("from intent = ${notiIntent.getStringExtra("money")}")
                val pendingIntent = PendingIntent.getActivity(this,0,notiIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

                val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("출금된 기록이 있는거 같아요!")
                    .setContentText(data+"원을 결제한게 맞나요?")
                    .setContentInfo("클릭해서 앱에 등록해주세요")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)


                // NotificationManager를 사용하여 알림을 표시합니다.
                val notificationManager =
                    this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                // Android 8.0 (API 레벨 26) 이상에서는 알림 채널을 생성해야 합니다.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channelName: CharSequence = "My Channel"
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel = NotificationChannel(channelId, channelName, importance)
                    notificationManager.createNotificationChannel(channel)
                }

                val notification = builder.build()

                notificationManager.notify(notificationId, notification)

            }
        }

    }
}
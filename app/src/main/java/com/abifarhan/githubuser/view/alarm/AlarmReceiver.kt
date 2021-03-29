package com.abifarhan.githubuser.view.alarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.abifarhan.githubuser.R
import com.abifarhan.githubuser.view.SplashActivity
import java.util.*

class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val DAILY_ID = 1
        const val NOTIFICATION_ID = 9
        const val CHANNEL_ID = "channel_id"
        const val CHANNEL_NAME = "channel_name"
        const val PESAN_HARIAN = "PESAN_RILIS"
        const val EXTRA_MESSAGE = "EXTRA_MESSAGE"
        const val EXTRA_NOTIFICATION = "EXTRA_NOTIFICATION"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val type = intent.getStringExtra(EXTRA_NOTIFICATION)
        if (type.equals(PESAN_HARIAN)) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val intent = Intent(context, SplashActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context,
                DAILY_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val builder: NotificationCompat.Builder =
                NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                    .setContentTitle("GithubUserApp")
                    .setContentText(message)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                builder.setChannelId(CHANNEL_ID)

                notificationManager.createNotificationChannel(channel)
            }

            val notification = builder.build()
            notificationManager.notify(NOTIFICATION_ID, notification)
        }

    }

    fun setNotificationForDaily(context: Context, string: String) {
        val info = context.resources.getString(R.string.open_the_app)
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(EXTRA_MESSAGE, info)
        intent.putExtra(EXTRA_NOTIFICATION, string)
        val calendar = Calendar.getInstance()
        calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DATE),
            9,
            0,
            0
        )
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, DAILY_ID, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context, "Notifikasi harian sudah aktif", Toast.LENGTH_SHORT).show()
    }

    fun turnOfNotificationForDaily(context: Context, string: String) {
        val alarmManager: AlarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context, DAILY_ID, intent, 0) as PendingIntent
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Notifikasi harian sudah dimatikan", Toast.LENGTH_SHORT).show()
    }
}
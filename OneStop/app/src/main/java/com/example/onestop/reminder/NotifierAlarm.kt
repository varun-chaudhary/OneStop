package com.example.onestop.reminder

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.onestop.MainActivity
import com.example.onestop.R
import com.example.onestop.reminder.AppDatabase.Companion.destroyInstance
import com.example.onestop.reminder.AppDatabase.Companion.getInstance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class NotifierAlarm : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val appDatabase = getInstance(context.applicationContext)
        val roomDAO = appDatabase.getRoomDAO()
        var reminder: Reminders
        GlobalScope.launch {
            reminder = roomDAO.getObjectUsingID(intent.getIntExtra("id", 0))
            roomDAO.Delete(reminder)
        }


        destroyInstance()
        val alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val intent1 = Intent(context, MainActivity::class.java)
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val taskStackBuilder = TaskStackBuilder.create(context)
        taskStackBuilder.addParentStack(MainActivity::class.java)
        taskStackBuilder.addNextIntent(intent1)
        val intent2 = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_IMMUTABLE)

        // NotificationCompat.Builder builder = new NotificationCompat.Builder(context);//by pranav
        val builder = NotificationCompat.Builder(context, "891")
        var channel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel =
                NotificationChannel("my_channel_02", "hello1", NotificationManager.IMPORTANCE_HIGH)
        }
        val notification: Notification = builder.setContentTitle("Reminder")
            .setContentText(intent.getStringExtra("Message")).setAutoCancel(true)
            .setSound(alarmsound).setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentIntent(intent2)
            .setChannelId("my_channel_02")
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel!!)
        }
        notificationManager.notify(1, notification)
    }


}
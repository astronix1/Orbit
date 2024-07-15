package com.example.myapp.com.example.myapp

import ExamWidgetProvider
import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Calendar

class UpdateWidgetReceiver : BroadcastReceiver(){
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val appWidgetManager = AppWidgetManager.getInstance(it)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(ComponentName(it, ExamWidgetProvider::class.java))
            ExamWidgetProvider.updateAppWidget(it, appWidgetManager, appWidgetIds)
        }
    }
    companion object {
        fun setDailyAlarm(context: Context) {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)

            val intent = Intent(context, UpdateWidgetReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

}
package com.example.medicineremainder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pillName = intent.getStringExtra("PILL_NAME") ?: "Your pill"
        val notificationHelper = NotificationHelper(context)
        notificationHelper.sendNotification(pillName)
    }
}

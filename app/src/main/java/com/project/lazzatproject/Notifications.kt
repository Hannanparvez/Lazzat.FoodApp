package com.project.lazzatproject

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class Notifications(){

val NOTIFY_TAG = "new notification"
    fun Notify(context: Context, message: String, number: Int){

        val intent = Intent(context, signin::class.java)

        val builder = NotificationCompat.Builder(context, String())
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Notification")
                .setContentText(message)
                .setNumber(number)
                .setSmallIcon(R.drawable.ic_person_black_24dp)
                .setContentIntent(PendingIntent.getActivity(context
                ,0,intent,PendingIntent.FLAG_UPDATE_CURRENT))
                .setAutoCancel(true)


        val notifManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


//        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.ECLAIR) {
            notifManager.notify(NOTIFY_TAG, 0, builder.build())
//        }else{
//            notifManager.notify(NOTIFY_TAG.hashCode(), builder.build())
//        }
    }
}
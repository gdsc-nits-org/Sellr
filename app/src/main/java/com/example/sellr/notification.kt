package com.example.sellr

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class notification : FirebaseMessagingService() {

    val channelName = "Sellr Notification"
    val channelId = "com.example.sellr"

    fun generateNotification(title: String, message: String) {


        //creating Intent so that if anyone clicks notification it redirects to the app

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT)



        //getRemoteView function to pass RemoteViews
        fun getRemoteView(title:String ,message:String ): RemoteViews{
            val remoteView = RemoteViews("com.example.sellr",R.layout.notificatioin_layout)

            remoteView.setTextViewText(R.id.name, title)
            remoteView.setTextViewText(R.id.message, message)

            return remoteView
        }


        //creating Notification
        var builder: NotificationCompat.Builder= NotificationCompat.Builder(applicationContext,channelId)
            .setSmallIcon(R.drawable.notificationbell)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        //attaching Notification Layout to Notification
        builder = builder.setContent(getRemoteView(title,message))

        //notification Manager
        val notificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId,channelName,
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0,builder.build())
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage){
        if(remoteMessage.getNotification()!=null)
        {
            generateNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
        }
    }

}


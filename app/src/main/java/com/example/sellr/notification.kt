package com.example.sellr

import android.app.PendingIntent
import android.content.Intent
import android.widget.RemoteViews
import com.google.firebase.messaging.FirebaseMessagingService

class notification : FirebaseMessagingService() {

    val channelName = "notification channel"
    val channelId = "com.example.pushnotificationpractice"

    fun generateNotification(title: String, message: String) {


        //creating Intent so that if anyone clicks notification it redirects to the app

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT)



        //getRemoteView function to pass RemoteViews
        fun getRemoteView(title:String ,message:String ): RemoteViews {
            val remoteView = RemoteViews("com.example.pushnotificationpractice", R.layout.notification)

            remoteView.setTextViewText(R.id.name, title)
            remoteView.setTextViewText(R.id.message, message)

            return remoteView
        }

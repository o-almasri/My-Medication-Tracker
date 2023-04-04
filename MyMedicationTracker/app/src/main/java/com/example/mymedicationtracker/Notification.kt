package com.example.mymedicationtracker

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat

const val notificationID = "NID"
const val notificationStatus = "NID"
const val channelID = "channel1"
const val channelID2 = "channel2"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

const val groupKey = "medication"
class Notification : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {

        val nstatus = intent.getBooleanExtra(notificationStatus,true)
        //if notification are turned off then push using low priority channel
        var notification = NotificationCompat.Builder(context, channelID2)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.play_store_512))
            .setGroup(groupKey)
            .build()

        if(nstatus){
             notification = NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(intent.getStringExtra(titleExtra))
                .setContentText(intent.getStringExtra(messageExtra))
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.play_store_512))
                .setGroup(groupKey)
                .build()
        }


        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val id = intent.getIntExtra(notificationID,0)

        manager.notify(id, notification)
    }

}
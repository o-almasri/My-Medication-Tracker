package com.example.mymedicationtracker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

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
    { Log.d("NOTIFICATION LOGS","0")
        val title = intent.getStringExtra(titleExtra)
        val desc = intent.getStringExtra(messageExtra)

        val sharedPreference = context.getSharedPreferences("Gson", Context.MODE_PRIVATE)
        val gson = Gson()
        val value = sharedPreference.getString("datelist", "null")
        val prefsEditor = sharedPreference.edit()
        Log.d("NOTIFICATION LOGS","1")
        if (value != "null" && value != "[]") {
            Log.d("NOTIFICATION LOGS","2")
            val myType = object : TypeToken<ArrayList<Date>>() {}.type
            val logs = gson.fromJson<ArrayList<Date>>(value, myType)
            val today = Date(Calendar.getInstance().timeInMillis)



            if (logs != null) {
                if(!have(logs,today)){
                    //Day is  not Blacklisted
                    notify(context , title , desc)
                }

            }

            // datelist is ready to read from now

        }else {
            notify(context , title , desc)
            Log.d("NOTIFICATION LOGS","5")
        }


        Log.d("NOTIFICATION LOGS","6")

    }

    fun notify(context: Context ,title:String? , desc:String? ){
        Log.d("NOTIFICATION LOGS","3")
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val action = NotificationCompat.Action.Builder(0, "Done", pendingIntent).build()

        val nstatus = intent.getBooleanExtra(notificationStatus,true)
        //if notification are turned off then push using low priority channel
        var notification = NotificationCompat.Builder(context, channelID2)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(desc)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.play_store_512))
            .setGroup(groupKey)
            .addAction(action)
            .build()

        if(nstatus){
            notification = NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(desc)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.play_store_512))
                .addAction(action)
                .setGroup(groupKey)
                .build()
        }


        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val id = intent.getIntExtra(notificationID,0)

        manager.notify(id, notification)
        Log.d("NOTIFICATION LOGS","4")
    }

        fun have(list:ArrayList<Date>,date:Date):Boolean{

            for(citem in list){
                if(citem.day == date.day)
                    if(citem.month == date.month)
                        if(citem.year == date.year)
                            return true


            }


            return false
        }

}
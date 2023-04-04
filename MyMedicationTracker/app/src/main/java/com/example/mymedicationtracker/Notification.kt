package com.example.mymedicationtracker

import android.app.NotificationManager
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

        val sharedPreference = context.getSharedPreferences("Gson", Context.MODE_PRIVATE)
        val gson = Gson()
        val value = sharedPreference.getString("datelist", "null")
        val prefsEditor = sharedPreference.edit()

        if (value != "null" && value != "[]") {
            val myType = object : TypeToken<ArrayList<Date>>() {}.type
            val logs = gson.fromJson<ArrayList<Date>>(value, myType)
            val today = Date(Calendar.getInstance().timeInMillis)

            if (logs != null) {
                if(!have(logs,today)){
                    //Toast.makeText(context, "TODAY IS BLACK LISTED", Toast.LENGTH_SHORT).show()
                    //Day is not Blacklisted so Display Notification Based on importance

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


            // datelist is ready to read from now

        }




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
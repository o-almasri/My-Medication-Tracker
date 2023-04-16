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
import java.io.Console
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

const val notificationID = "NID"
const val notificationStatus = "NID"
const val channelID = "channel1"
const val channelID2 = "channel2"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"
const val msgitem = "myitem"

const val parceltitle = "parceltitle"
const val parceldesc = "parceldesc"
const val parceldate = "parceldate"
const val parceldose = "parceldose"
const val parceltype = "parceltype"
const val parcelid = "parcelid"


const val groupKey = "medication"
class Notification : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    { Log.d("NOTIFICATION LOGS","0")
        val title = intent.getStringExtra(titleExtra)
        val desc = intent.getStringExtra(messageExtra)


        val rparceltitle = intent.getStringExtra(parceltitle)!!
        val rparceldesc = intent.getStringExtra(parceldesc)!!
        val rparceldate = Date().time
        val rparceldose = intent.getStringExtra(parceldose)!!
        val rparceltype = intent.getStringExtra(parceltype)!!
        val rparcelid = intent.getStringExtra(parcelid)!!
        var rparcelnstatus = intent.getBooleanExtra(notificationStatus,true)!!

        Log.d("Notification","ID is IS  ${rparcelid}")

        val obj = historyitem(rparceltitle,rparceldesc,Date(rparceldate),rparceldose,rparceltype,rparcelid,rparcelnstatus)


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
                    //Day is  not Blacklisted
                    Log.d("NOTIFICATION LOGS","1")
                    notify(context , title , desc,obj)
                }

            }

            // datelist is ready to read from now

        }else {
            Log.d("NOTIFICATION LOGS","2")
            notify(context , title , desc,obj)

        }




    }

    fun notify(context: Context ,title:String? , desc:String? ,obj:historyitem){


        val intent = Intent(context, history::class.java)
        intent.putExtra("mode","add")

        Log.d("Notification","OBJECT IS  ${obj}")
        intent.putExtra("parceltitle", obj.Title)
        intent.putExtra("parceldesc", obj.desc)
        intent.putExtra("parceldate", obj.date.time.toString())
        intent.putExtra("parceldose", obj.dose)
        intent.putExtra("parceltype", obj.type)
        intent.putExtra("parcelid", obj.ID)
        intent.putExtra("NID", obj.nstatus)
        Log.d("Parcil From Notification","trueorFalse its :"+obj.nstatus)

        val receivedItem = intent.getParcelableExtra<historyitem>("myitem")
        intent.putExtra("myitem",receivedItem)


        var pendingIntent = PendingIntent.getActivity(context, obj.ID.toInt(), intent, 0)
        //TODO:: make action button auto cancel Notification when clicked
        val action = NotificationCompat.Action.Builder(0, "Done", pendingIntent).build()

        val nstatus = intent.getBooleanExtra(notificationStatus,true)
        //if notification are turned off then push using low priority channel
        var notification = NotificationCompat.Builder(context, channelID2)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(desc)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.play_store_512))
            .setGroup(groupKey)
            .setAutoCancel(true)
            .addAction(action)
            .build()

        if(nstatus){
            notification = NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(desc)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources,R.drawable.play_store_512))
                .addAction(action)
                .setAutoCancel(true)
                .setGroup(groupKey)
                .build()
        }


        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        manager.notify(obj.ID.toInt(), notification)
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
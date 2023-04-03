package com.example.mymedicationtracker

import android.app.*
import android.app.PendingIntent.getBroadcast
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.ref.ReferenceQueue
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() , View.OnClickListener{

    lateinit var addbtn: Button;
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: newadapter
    lateinit var recyclerViewManager: RecyclerView.LayoutManager
    lateinit var list:ArrayList<entry>


    private val CHANNEL_ID = "channel_id_example_111"
    private val notificationId = 10

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       


        addbtn = findViewById(R.id.addbtn);
        addbtn.setOnClickListener(this);

        recyclerView = findViewById(R.id.RCV)
        recyclerViewManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = recyclerViewManager

        //get list from storage and apply it
         list = arrayListOf<entry>()
        val sharedPreference =  getSharedPreferences("Gson", Context.MODE_PRIVATE)
        var gson = Gson()
        val value = sharedPreference.getString("list","null");


        if(value !="null" && value != "[]"){


            val myType = object : TypeToken<ArrayList<entry>>() {}.type
            val logs = gson.fromJson<ArrayList<entry>>(value, myType)
            if(logs != null){
            for (citem in logs) {

                list.add(
                    entry(
                        citem.name,citem.times,citem.dmy,citem.startdate , citem.completed , citem.tdoes ,citem.current , citem.myID
                    )
                )
            }
                //Toast.makeText(this, logs[0].tdoes.toString(), Toast.LENGTH_LONG).show();

            }



        }else {
            //nothing found
            //Toast.makeText(this, " List is 1 Empty", Toast.LENGTH_SHORT).show();

        }

        if(list.isNotEmpty()){
            //setup recyclerView
            recyclerView = findViewById(R.id.RCV)
            recyclerViewManager = LinearLayoutManager(applicationContext)
            recyclerView.layoutManager = recyclerViewManager
            recyclerView.setHasFixedSize(true)
            recyclerAdapter = newadapter(list);
            recyclerView.adapter = recyclerAdapter;
        }




    }

    fun refresh(){

        val sharedPreference =  getSharedPreferences("Gson", Context.MODE_PRIVATE)
        val pref = getSharedPreferences("Gson", Context.MODE_PRIVATE);
        var gson = Gson()
        val value = sharedPreference.getString("list","null");
       if(value != "null"){
           list = arrayListOf<entry>()
           val myType = object : TypeToken<ArrayList<entry>>() {}.type

           val logs = gson.fromJson<ArrayList<entry>>(value, myType)
           if(logs.isNotEmpty())
           if(!logs[0].name.isNullOrBlank()){
               for (citem in logs) {
                   list.add(
                       entry(
                           citem.name,citem.times,citem.dmy,citem.startdate,citem.completed , citem.tdoes , citem.current ,citem.myID
                       )
                   )
               }
               recyclerAdapter = newadapter(list);
               recyclerView.adapter = recyclerAdapter;
               recyclerAdapter.notifyDataSetChanged();
           }//is null
       }



    }


    override fun onResume() {
        super.onResume()
        refresh()
       // sendNotification()
    }
    override fun onPause() {
        super.onPause()
       // Toast.makeText(this, " Paused", Toast.LENGTH_SHORT).show();

    }






    override fun onClick(btn: View?) {


        when(btn){
            addbtn->{
                //go to activity
                val intent = Intent(this , Add_entry::class.java);
                intent.putExtra("mode","new")
                startActivity(intent);
            }

        }
    }



    private fun showAlert(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->}
            .show()
    }

    private fun getTime(): Long
    {
        val minute = LocalDateTime.now().minute+1
        val hour = LocalDateTime.now().hour
        val day = LocalDateTime.now().dayOfMonth
        val month = LocalDateTime.now().monthValue
        val year = LocalDateTime.now().year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }














}
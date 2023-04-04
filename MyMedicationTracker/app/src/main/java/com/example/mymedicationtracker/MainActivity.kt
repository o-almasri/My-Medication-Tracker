package com.example.mymedicationtracker

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() , View.OnClickListener {
    lateinit var addbtn: Button;
    lateinit var settingbtn: Button;
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: newadapter
    lateinit var recyclerViewManager: RecyclerView.LayoutManager
    lateinit var list:ArrayList<entry>


/*



THIS IS THE WORKING ONE





 */

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MSG","MAIN GOT SOMETHING")


        addbtn = findViewById(R.id.addbtn);
        addbtn.setOnClickListener(this);
        settingbtn = findViewById(R.id.settingsButton);
        settingbtn.setOnClickListener(this);

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
                            citem.name,citem.times,citem.dmy,citem.startdate , citem.completed , citem.tdoes ,citem.current , citem.myID , citem.notificationStatus
                        )
                    )
                }


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
                                citem.name,citem.times,citem.dmy,citem.startdate,citem.completed , citem.tdoes , citem.current ,citem.myID ,citem.notificationStatus
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
                val intent = Intent(applicationContext,Add_entry::class.java)
                intent.putExtra("mode","new")
                startActivity(intent);
            }
            settingbtn->{
                //go to activity
                val intent = Intent(applicationContext,settings::class.java)
                intent.putExtra("mode","new")
                startActivity(intent);
            }


        }
    }




}
package com.example.mymedicationtracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.ref.ReferenceQueue

class MainActivity : AppCompatActivity() , View.OnClickListener{

    lateinit var addbtn: Button;
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: adapter
    lateinit var recyclerViewManager: RecyclerView.LayoutManager
    lateinit var list:ArrayList<entry>








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        addbtn = findViewById(R.id.addbtn);
        addbtn.setOnClickListener(this);



        //get list from storage and apply it
         list = arrayListOf<entry>()
        val sharedPreference =  getSharedPreferences("Gson", Context.MODE_PRIVATE)
        val pref = getSharedPreferences("Gson", Context.MODE_PRIVATE);
        var gson = Gson()
        val value = sharedPreference.getString("list","null");
        val prefsEditor = pref.edit()
        var serilizedArray  =  pref.getString("list","NULL");

        if(value !="NULL" && value != "[]"){


            val myType = object : TypeToken<ArrayList<entry>>() {}.type
            val logs = gson.fromJson<ArrayList<entry>>(value, myType)
            if(logs != null)
            for (citem in logs) {

                list.add(
                    entry(
                        citem.name,citem.times,citem.dmy,citem.startdate
                    )
                )
            }


        }else {
            //nothing found
            Toast.makeText(this, " List is Empty", Toast.LENGTH_SHORT).show();

        }


        //setup recyclerView
        recyclerView = findViewById(R.id.RCV)
        recyclerViewManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = recyclerViewManager
        recyclerView.setHasFixedSize(true)



        recyclerAdapter = adapter(list);
        recyclerView.adapter = recyclerAdapter;











    }

    fun refresh(){

        val sharedPreference =  getSharedPreferences("Gson", Context.MODE_PRIVATE)
        val pref = getSharedPreferences("Gson", Context.MODE_PRIVATE);
        var gson = Gson()
        val value = sharedPreference.getString("list","null");
        list = arrayListOf<entry>()
        val myType = object : TypeToken<ArrayList<entry>>() {}.type
        val logs = gson.fromJson<ArrayList<entry>>(value, myType)
        for (citem in logs) {
            list.add(
                entry(
                    citem.name,citem.times,citem.dmy,citem.startdate
                )
            )
        }

        recyclerAdapter = adapter(list);
        recyclerView.adapter = recyclerAdapter;
        recyclerAdapter.notifyDataSetChanged();

    }


    override fun onResume() {
        super.onResume()
        refresh()
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
                startActivity(intent);
            }

        }
    }





}
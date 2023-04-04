package com.example.mymedicationtracker

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

class settings : AppCompatActivity() , View.OnClickListener {
    lateinit var datePicker: DatePicker
    lateinit var snoozbtn:Button
    lateinit var clearbtn:Button
    lateinit var historybtn:Button
    lateinit var recyclerView: RecyclerView


    lateinit var recyclerAdapter: settingsadapter
    lateinit var recyclerViewManager: RecyclerView.LayoutManager
    lateinit var list: java.util.ArrayList<Date>
    lateinit var builder: AlertDialog.Builder
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        datePicker = findViewById(R.id.datePicker)
        snoozbtn = findViewById(R.id.snoozbtn)
        clearbtn = findViewById(R.id.backbtn)
        recyclerView = findViewById(R.id.historyrecyclerView)
        historybtn = findViewById(R.id.gotohistorybtn)
        snoozbtn.setOnClickListener(this)
        clearbtn.setOnClickListener(this)
        historybtn.setOnClickListener(this)

        recyclerViewManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = recyclerViewManager





        updateRecycleView()




    }

    fun updateRecycleView(){


        recyclerView = findViewById(R.id.historyrecyclerView)
        recyclerViewManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = recyclerViewManager
        //get list from storage and apply it
        list = arrayListOf<Date>()
        val sharedPreference =  getSharedPreferences("Gson", Context.MODE_PRIVATE)
        var gson = Gson()
        val value = sharedPreference.getString("datelist","null");


        if(value !="null" && value != "[]"){

            val myType = object : TypeToken<java.util.ArrayList<Date>>() {}.type
            val logs = gson.fromJson<java.util.ArrayList<Date>>(value, myType)
            if(logs != null){
                    list.addAll(logs)
            }
        }else {
            //nothing found
            //Toast.makeText(this, " List is 1 Empty", Toast.LENGTH_SHORT).show();

        }


        if(list.isNotEmpty()){
            //setup recyclerView
            recyclerView.setHasFixedSize(true)
            recyclerAdapter = settingsadapter(list);
            recyclerView.adapter = recyclerAdapter;
            recyclerAdapter.notifyDataSetChanged();
        }


    }





    fun getDateFromDatePicker(datePicker: DatePicker): String {
        val day = datePicker.dayOfMonth
        val month = datePicker.month  // month is 0-indexed, so add 1
        val year = datePicker.year

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month , day)


        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)


        val date: Date = calendar.getTime()

        //store this value in shared preferences
        storeDate2(date)
        // Format the date as a string and return it
        return "$day/$month/$year"
    }



    fun storeDate2(date: Date) {
        // Get list from storage and apply it
        val sharedPreference = getSharedPreferences("Gson", Context.MODE_PRIVATE)
        val gson = Gson()
        val value = sharedPreference.getString("datelist", "null")
        val prefsEditor = sharedPreference.edit()
        var datelist = arrayListOf<Date>()
        if (value != "null" && value != "[]") {
            val myType = object : TypeToken<ArrayList<Date>>() {}.type
            val logs = gson.fromJson<ArrayList<Date>>(value, myType)

            if (logs != null) {
                // Check for duplicates

                datelist.addAll(logs)
                if (!have(datelist,date)) {
                    datelist.add(date)
                    //Toast.makeText(this, "Date added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Date already exists", Toast.LENGTH_SHORT).show()
                }
            }

            // datelist is ready to read from now
            val myarray = gson.toJson(datelist)
            prefsEditor.putString("datelist", myarray)
            prefsEditor.apply()

        } else {
            // First time initialization
            val mylistArray = ArrayList<Date>()
            mylistArray.add(date)

            val temp = gson.toJson(mylistArray)
            prefsEditor.putString("datelist", temp)
            prefsEditor.apply()
            //Toast.makeText(this, "First time initialization success", Toast.LENGTH_SHORT).show()
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


    override fun onClick(v: View?) {
        when(v){
            snoozbtn->{
                getDateFromDatePicker(datePicker)
               // Toast.makeText(getApplicationContext(), "Selected Date: " + getDateFromDatePicker(datePicker), Toast.LENGTH_SHORT).show();
                updateRecycleView()
            }
            clearbtn->{
                // First time initialization

                val sharedPreference = getSharedPreferences("Gson", Context.MODE_PRIVATE)
                val gson = Gson()
                val prefsEditor = sharedPreference.edit()
                val mylistArray = ArrayList<Date>()

                val temp = gson.toJson(mylistArray)
                prefsEditor.putString("datelist", temp)
                prefsEditor.apply()
                Toast.makeText(this, "All Data Removed successfully", Toast.LENGTH_SHORT).show()


                recyclerView.setHasFixedSize(true)
                recyclerAdapter = settingsadapter(mylistArray);
                recyclerView.adapter = recyclerAdapter;
                recyclerAdapter.notifyDataSetChanged();

            }

            historybtn->{

                val intent = Intent(v.context,history::class.java)
                startActivity(intent);
            }
        }
    }
}
package com.example.mymedicationtracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class history : AppCompatActivity(), View.OnClickListener  {

    lateinit var backbtn: Button
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: historyadapter
    lateinit var recyclerViewManager: RecyclerView.LayoutManager
    lateinit var list: java.util.ArrayList<historyitem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        backbtn = findViewById(R.id.backbtn)
        recyclerView = findViewById(R.id.historyrecyclerView)
        backbtn.setOnClickListener(this)

        recyclerViewManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = recyclerViewManager

        updateRecycleView()


// To receive the extras in MyActivity

        val receivedExtras = intent.extras
        val mode = receivedExtras?.getString("mode")

        if(mode == "add"){
            val rparceltitle = receivedExtras?.getString("parceltitle")!!
            val rparceldesc = receivedExtras?.getString("parceldesc")!!
            val rparceldate = receivedExtras?.getString("parceldate")!!
            val rparceldose = receivedExtras?.getString("parceldose")!!
            val rparceltype = receivedExtras?.getString("parceltype")!!
            val rparcelid = receivedExtras?.getString("parcelid")!!
             var    obj = historyitem(rparceltitle,rparceldesc, Date(),rparceldose,rparceltype,rparcelid)

            Log.d("Datalist","Added 1 element ${obj}")
            storeDate2(obj!!)

            //add 1 Dose number to the Progress
            adddose(rparcelid,rparceldose.toInt())




        }




    }


    fun adddose(id:String,dose:Int){
        //get stored array

        val pref = getSharedPreferences("Gson", Context.MODE_PRIVATE);
        val prefsEditor = pref.edit()
        val gson = Gson()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        var serilizedArray = pref.getString("list", "NULL");

        if (serilizedArray != "NULL") {


            val myType = object : TypeToken<java.util.ArrayList<entry>>() {}.type
            val logs = gson.fromJson<java.util.ArrayList<entry>>(serilizedArray, myType)

           //search the array for the element
            for(citem in logs){
                if(citem.myID == id){
                    Log.d("Datalist","Found My Element")
                    citem.current +=dose
                }
            }

            var marray = gson.toJson(logs);
            prefsEditor.putString("list", marray);
            prefsEditor.apply()




        }

        //search it
        //adjust element
        //save it



    }

    override fun onClick(v: View?) {
        when(v){
            backbtn->{
                val intent = Intent(v.context,MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent);
            }
        }
    }

    fun updateRecycleView(){


        recyclerView = findViewById(R.id.historyrecyclerView)
        recyclerViewManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = recyclerViewManager
        //get list from storage and apply it
        list = arrayListOf<historyitem>()
        val sharedPreference =  getSharedPreferences("Gson", Context.MODE_PRIVATE)
        var gson = Gson()
        val value = sharedPreference.getString("historylist","null");


        if(value !="null" && value != "[]"){

            val myType = object : TypeToken<java.util.ArrayList<historyitem>>() {}.type
            val logs = gson.fromJson<java.util.ArrayList<historyitem>>(value, myType)
            if(logs != null){
                list.addAll(logs)
            }
        }else {
            //nothing found


        }


        if(list.isNotEmpty()){
            //setup recyclerView
            recyclerView.setHasFixedSize(true)
            recyclerAdapter = historyadapter(list);
            recyclerView.adapter = recyclerAdapter;
            recyclerAdapter.notifyDataSetChanged();
        }


    }

    fun storeDate2(date: historyitem) {
        if(date == null){
            Log.d("Error","StoreDate2 Date is null")
            return

        }
        Log.d("Datalist","Element to be Added ${date}")
        // Get list from storage and apply it
        val sharedPreference = getSharedPreferences("Gson", Context.MODE_PRIVATE)
        val gson = Gson()
        val value = sharedPreference.getString("historylist", "null")
        val prefsEditor = sharedPreference.edit()
        var datelist = arrayListOf<historyitem>()

        if (value != "null" && value != "[]") {
            Log.d("StoreData2","value is ${value}")
            val myType = object : TypeToken<ArrayList<historyitem>>() {}.type
            val logs = gson.fromJson<ArrayList<historyitem>>(value, myType)

            if (logs != null) {
                // Check for duplicates

                datelist.addAll(logs)

                    datelist.add(date)
                    Log.d("Datalist","Added 1 element ${logs}")
                    Toast.makeText(this, "Date added successfully", Toast.LENGTH_SHORT).show()

            }

            // datelist is ready to read from now
            val myarray = gson.toJson(datelist)
            prefsEditor.putString("historylist", myarray)
            prefsEditor.apply()

        } else {
            // First time initialization
            val mylistArray = ArrayList<historyitem>()
            mylistArray.add(date)

            val temp = gson.toJson(mylistArray)
            prefsEditor.putString("historylist", temp)
            prefsEditor.apply()
            Log.d("Datalist","First Time initilization Added 1 element ")
            Toast.makeText(this, "First time initialization success", Toast.LENGTH_SHORT).show()

        }

        updateRecycleView()
    }

    fun have(list:ArrayList<historyitem>, item: historyitem):Boolean{

        for(citem in list){
            if(citem.ID == item.ID)
                        return true
        }

        return false
    }


}



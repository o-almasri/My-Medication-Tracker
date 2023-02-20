package com.example.mymedicationtracker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Add_entry : AppCompatActivity()  ,View.OnClickListener {

    lateinit var MedName: EditText;
    lateinit var Medtimes:EditText;
    lateinit var RG:RadioGroup;
    lateinit var Radiobtn:RadioButton;
    lateinit var submit: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        MedName  = findViewById(R.id.medicationNameID)
        Medtimes  = findViewById(R.id.medicationtimesID)
        submit  = findViewById(R.id.submitbtn)
        RG = findViewById(R.id.RG1)

        //adding onclick listener
        submit.setOnClickListener(this);


        //retreive the data

        val sharedPreference =  getSharedPreferences("Gson", Context.MODE_PRIVATE)
        val value = sharedPreference.getString("list","null");
        var gson = Gson()
        if(value != null){
            val slist = gson.fromJson<MutableList<entry>>(value, mutableListOf<entry>().javaClass);
            if(slist != null){
              //  Toast.makeText(applicationContext,slist.toString(), Toast.LENGTH_LONG).show()
            }
        }





    }//onclick


    override fun onClick(p0: View?) {
        when (p0) {


            submit -> {
                Radiobtn = findViewById(RG.checkedRadioButtonId)
                if (MedName.text.isNullOrBlank()) {
                    Toast.makeText(this, " Please Fill Both Feilds ", Toast.LENGTH_SHORT).show();
                } else
                    if (Medtimes.text.isNullOrBlank()) {
                        Toast.makeText(this, " Please Fill Both Feilds ", Toast.LENGTH_SHORT)
                            .show();
                    } else {
                        //save the string


                        val pref = getSharedPreferences("Gson", Context.MODE_PRIVATE);
                        val prefsEditor = pref.edit()
                        val gson = Gson()
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        val current = LocalDateTime.now().format(formatter)
                        var serilizedArray = pref.getString("list", "NULL");

                        if (serilizedArray != "NULL") {


                            //val gson = Gson()
                            val myType = object : TypeToken<ArrayList<entry>>() {}.type
                            val logs = gson.fromJson<ArrayList<entry>>(serilizedArray, myType)

                            logs.add(
                                entry(
                                    MedName.text.toString(),
                                    Medtimes.text.toString().toInt(),
                                    Radiobtn.text.toString(),
                                    current.toString()
                                )
                            )

                            var marray = gson.toJson(logs);
                            prefsEditor.putString("list", marray);
                            prefsEditor.apply()
                            Toast.makeText(this, " Success", Toast.LENGTH_SHORT).show();

                            finish()

                        } else {

                            //first time initilization

                            val pref = getSharedPreferences("Gson", Context.MODE_PRIVATE);
                            val prefsEditor = pref.edit()
                            var mylistArray = ArrayList<entry>();
                            mylistArray.add(
                                entry(
                                    MedName.text.toString(),
                                    Medtimes.text.toString().toInt(),
                                    Radiobtn.text.toString(),
                                    current.toString()
                                )
                            )
                            val gson = Gson()
                            var temp = gson.toJson(mylistArray);
                            prefsEditor.putString("list", temp)
                            prefsEditor.apply()
                            Toast.makeText(this, " new List Created Success", Toast.LENGTH_SHORT)
                                .show();

                        }


                    }
            }
        }
    }



}//main

package com.example.mymedicationtracker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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



        val sharedPreference =  getSharedPreferences("data", Context.MODE_PRIVATE)
        val value = sharedPreference.getString("jsonString","null");
        //comment 


    }//onclick


    override fun onClick(p0: View?) {
        when (p0)
        {




            submit->{

                val entrees: MutableList<entry> = mutableListOf()
                entrees.add(entry("Test",3 , "d" , "06/02/2023"))
                var gson = Gson()
                var jsonString = gson.toJson(entrees);

                val sharedPreference =  getSharedPreferences("data", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putString("jsonString",jsonString);

                editor.commit()









                Radiobtn = findViewById(RG.getCheckedRadioButtonId())
                Toast.makeText(applicationContext,"saved", Toast.LENGTH_SHORT).show()
            }
        }
    }



}//main

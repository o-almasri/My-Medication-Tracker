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
    lateinit var rday:RadioButton;
    lateinit var rmonth:RadioButton;
    lateinit var ryear:RadioButton;
    lateinit var submit: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        MedName  = findViewById(R.id.medicationNameID)
        Medtimes  = findViewById(R.id.medicationtimesID)
        submit  = findViewById(R.id.submitbtn)

        rday = findViewById(R.id.rday)
        rmonth = findViewById(R.id.rmonth)
        ryear = findViewById(R.id.ryear)

        RG = findViewById(R.id.RG1)

        //adding onclick listener
        submit.setOnClickListener(this);

        val action =intent.getStringExtra("mode")


        //retreive the data

        val sharedPreference =  getSharedPreferences("Gson", Context.MODE_PRIVATE)
        val value = sharedPreference.getString("list","null");
        var gson = Gson()
        if(value != null){
            val slist = gson.fromJson<MutableList<entry>>(value, mutableListOf<entry>().javaClass);
            if(slist != null){
              
            }
        }
    if(action == "edit"){
        submit.setText("Done");
        //edit mode so fill the fields
        val passedstring = intent.getStringExtra("content");

        if(!passedstring.isNullOrBlank()){

          //  val item = gson.fromJson<MutableList<entry>>(passedstring, mutableListOf<entry>().javaClass);
           // Toast.makeText(applicationContext,item.toString(), Toast.LENGTH_LONG).show()

            val myType = object : TypeToken<ArrayList<entry>>() {}.type
            val logs = gson.fromJson<ArrayList<entry>>(passedstring, myType)
            //Toast.makeText(applicationContext,logs[0].times.toString(), Toast.LENGTH_LONG).show()
            if(logs != null){
                MedName.setText(logs[0].name)
                Medtimes.setText(logs[0].times.toString());
                setIDfromText(logs[0].dmy)
            }

        }

    }




    }//onclick

fun setIDfromText(str:String){


    if(str.equals(getString(R.string.day))) {
        RG.check(R.id.rday)
    }else if(str.equals(getString(R.string.month))){
        RG.check(R.id.rmonth)
    } else if(str.equals(getString(R.string.year))){
        Toast.makeText(applicationContext,"year was true", Toast.LENGTH_LONG).show()
        RG.check(R.id.ryear)
    }


}

    override fun onPause() {
        super.onPause()


    }

    override fun onStop() {
        super.onStop()

        //save data before exiting

    }

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

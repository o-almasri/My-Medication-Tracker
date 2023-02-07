package com.example.mymedicationtracker

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

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





    }//onclick


    override fun onClick(p0: View?) {
        when (p0)
        {





            submit->{
                Radiobtn = findViewById(RG.getCheckedRadioButtonId())
                Toast.makeText(applicationContext,"saved", Toast.LENGTH_SHORT).show()
            }
        }
    }



}//main

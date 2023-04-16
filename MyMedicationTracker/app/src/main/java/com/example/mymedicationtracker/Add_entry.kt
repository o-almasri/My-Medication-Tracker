package com.example.mymedicationtracker

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random


class Add_entry : AppCompatActivity()  , View.OnClickListener {

    lateinit var MedName: EditText;
    lateinit var Medtimes: EditText;
    lateinit var medamount: EditText;
    lateinit var RG: RadioGroup;
    lateinit var Radiobtn: RadioButton;
    lateinit var Radiotype: RadioButton;
    lateinit var rday: RadioButton;
    lateinit var rmonth: RadioButton;
    lateinit var ryear: RadioButton;
    lateinit var submit: Button;
    lateinit var switch: Switch
    lateinit var progresscurrent: EditText;
    lateinit var totaldosecurrent: EditText;
    lateinit var myID: TextView;
    lateinit var myIDvalue : String

    lateinit var RG2: RadioGroup;
    lateinit var pillsbtn: RadioButton;
    lateinit var mgbtn: RadioButton;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_entry)

        MedName  = findViewById(R.id.medicationNameID)
        Medtimes  = findViewById(R.id.medicationtimesID2)
        medamount  = findViewById(R.id.medamount)

        submit  = findViewById(R.id.submitbtn)

        progresscurrent = findViewById(R.id.progressid)
        totaldosecurrent = findViewById(R.id.totalid)
        RG2 = findViewById(R.id.rg2)
        pillsbtn = findViewById(R.id.rbpills)
        mgbtn = findViewById(R.id.rbmg)
        switch = findViewById(R.id.switch1)

        myID = findViewById(R.id.myID)
        myIDvalue = ""

        rday = findViewById(R.id.rday)
        rmonth = findViewById(R.id.rmonth)
        ryear = findViewById(R.id.ryear)

        RG = findViewById(R.id.RG1)


        Radiotype = findViewById(RG2.checkedRadioButtonId)
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
                    //is completed now will be calculated
                    switch.isChecked = logs[0].notificationStatus

                    progresscurrent.setText(logs[0].current.toString())
                    totaldosecurrent.setText(logs[0].tdoes.toString())
                    myID.setText(logs[0].myID)
                    myIDvalue = logs[0].myID

                    //check if type is pills or other
                    setIDfromText(logs[0].type)
                    medamount.setText(logs[0].amount)




                    val title = MedName.text.toString()

                    val date = Date()
                    val dose = medamount.text.toString()
                    val type = Radiotype.text.toString()
                    val id = myIDvalue
                    val nstatus = logs[0].notificationStatus;
                    val desc = "Time To Take $dose $type of your $title Medication "
                    val item = historyitem(title, desc, date, dose, type, id,nstatus)
                    stopnotificationforelement(logs[0].myID,applicationContext,item)
                }

            }

        }




    }//onclick

    fun setIDfromText(str:String){

        Log.d("WHICH ONE",str)
        if(str.equals(getString(R.string.day))) {
            RG.check(R.id.rday)
        }else if(str.equals(getString(R.string.month))){
            RG.check(R.id.rmonth)
        } else if(str.equals(getString(R.string.year))){
            // Toast.makeText(applicationContext,"year was true", Toast.LENGTH_LONG).show()
            RG.check(R.id.ryear)
        }

        if(str.equals("Pills/Tablets")){
            RG2.check(R.id.rbpills)
        }else if(str.equals("Liquids/mg")){
            RG2.check(R.id.rbmg)
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
                Radiotype = findViewById(RG2.checkedRadioButtonId)
                if (MedName.text.isNullOrBlank()) {
                    Toast.makeText(this, " Please Fill Both Feilds ", Toast.LENGTH_SHORT).show();
                } else
                    if (Medtimes.text.isNullOrBlank()) {
                        Toast.makeText(this, " Please Fill Both Feilds ", Toast.LENGTH_SHORT)
                            .show();
                    } else {
                        //save the string

                        if(totaldosecurrent.text.toString().isNullOrBlank()){
                            totaldosecurrent.setText("1");
                        }
                        if(progresscurrent.text.toString().isNullOrBlank()){
                            progresscurrent.setText("0");
                        }

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
                            val rand = Random.Default
                            var notificationuid = rand.nextInt(9999-1000)+1000

                            if(myIDvalue.isNullOrBlank()){
                                myIDvalue = notificationuid.toString()
                            }
                            if(totaldosecurrent.text.toString().toInt()<=0){
                                totaldosecurrent.setText("1");
                            }
                            if(progresscurrent.text.toString().toInt()<=0){
                                progresscurrent.setText("0");
                            }
                            if(progresscurrent.text.toString().toInt()<=totaldosecurrent.text.toString().toInt()){
                                totaldosecurrent.setText(totaldosecurrent.text.toString());
                            }
                            Log.d("Radiobutton is ",Radiotype.text.toString())
                            var percentage = 0;
                            if(progresscurrent.text.toString().toInt()>0)
                                percentage = totaldosecurrent.text.toString().toInt()/progresscurrent.text.toString().toInt()+1
                            logs.add(
                                entry(
                                    //name
                                    MedName.text.toString(),
                                    //how many times
                                    Medtimes.text.toString().toInt(),
                                    //day month or year
                                    Radiobtn.text.toString(),
                                    // date
                                    current.toString(),
                                    //is completed
                                    (percentage>=1),
                                    //total doeses
                                    totaldosecurrent.text.toString().toInt(),
                                    //current progress
                                    progresscurrent.text.toString().toInt(),
                                    // my notification ID
                                    myIDvalue ,
                                    switch.isChecked ,
                                    Radiotype.text.toString(),
                                    medamount.text.toString()




                                )
                            )

                            var marray = gson.toJson(logs);
                            prefsEditor.putString("list", marray);
                            prefsEditor.apply()
                            Toast.makeText(this, " Success", Toast.LENGTH_SHORT).show();

                            //schedule notification
                           // save_notification_ID(myIDvalue);
                            //add its ID into notification IDS
                            val title = MedName.text.toString()
                            val desc = "My Description"
                            val date = Date()
                            val dose = medamount.text.toString()
                            val type = Radiotype.text.toString()
                            val id = myIDvalue

                            val item = historyitem(title, desc, date, dose, type, id,switch.isChecked)

                            val left = totaldosecurrent.text.toString().toInt() -progresscurrent.text.toString().toInt()
                            scheduleNotification(getrepeatingTimeinterval(Radiobtn.text.toString(),Medtimes.text.toString().toLong()) ,
                                MedName.text.toString(),left.toString(),switch.isChecked,Radiotype.text.toString(),medamount.text.toString()
                            ,item
                            )


                        } else {

                            //first time initilization

                            val pref = getSharedPreferences("Gson", Context.MODE_PRIVATE);
                            val prefsEditor = pref.edit()
                            var mylistArray = ArrayList<entry>();
                            val rand = Random.Default
                            var notificationuid = rand.nextInt(99999-10000)+10000

                            myIDvalue = notificationuid.toString()
                            myID.setText(myIDvalue)
                            //schedule notification
                         //  save_notification_ID(myIDvalue);


                            var percentage = 0;
                            if(progresscurrent.text.toString().toInt()>0)
                             percentage = totaldosecurrent.text.toString().toInt()/progresscurrent.text.toString().toInt()
                            mylistArray.add(
                                entry(
                                    MedName.text.toString(),
                                    Medtimes.text.toString().toInt(),
                                    Radiobtn.text.toString(),
                                    current.toString(),
                                    (percentage)>=1,
                                    //switch.isChecked ,
                                    totaldosecurrent.text.toString().toInt(),
                                    progresscurrent.text.toString().toInt(),
                                    // my notification ID
                                    myIDvalue,
                                    switch.isChecked ,
                                    Radiotype.text.toString(),
                                    medamount.text.toString()
                                )
                            )
                            val gson = Gson()
                            var temp = gson.toJson(mylistArray);
                            prefsEditor.putString("list", temp)
                            prefsEditor.apply()
                            Toast.makeText(this, " Success", Toast.LENGTH_SHORT).show();

                            val title = MedName.text.toString()
                            val date = Date()
                            val dose = medamount.text.toString()
                            val type = Radiotype.text.toString()
                            val id = myIDvalue
                            val desc = "Time To Take $dose $type of your $title Medication "


                            val item = historyitem(title, desc, date, dose, type, id ,switch.isChecked)


                            //progress left
                            val left = totaldosecurrent.text.toString().toInt() -progresscurrent.text.toString().toInt()


                            scheduleNotification(getrepeatingTimeinterval(Radiobtn.text.toString(),Medtimes.text.toString().toLong()) ,
                                MedName.text.toString(),left.toString(),switch.isChecked,Radiotype.text.toString(),medamount.text.toString()
                                ,item
                            )




                        }


                    }


            }
        }

    }

    private fun save_notification_ID(id:String) {


        val pref = getSharedPreferences("Gson", Context.MODE_PRIVATE);
        val prefsEditor = pref.edit()
        val gson = Gson()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val current = LocalDateTime.now().format(formatter)
        var serilizedArray = pref.getString("idslist", "NULL");

        if (serilizedArray != "NULL") {


            //val gson = Gson()
            val myType = object : TypeToken<ArrayList<saveids>>() {}.type
            val logs = gson.fromJson<ArrayList<saveids>>(serilizedArray, myType)


            if (myIDvalue.isNullOrBlank()) {
                return
            }

            logs.add(
                saveids(
                    //ID we want to save
                    myIDvalue
                )
            )

            var marray = gson.toJson(logs);
            prefsEditor.putString("idslist", marray);
            prefsEditor.apply()
            Toast.makeText(this, "ID Saved Successfully", Toast.LENGTH_SHORT).show();


        }
    }
    private fun getrepeatingTimeinterval(dmy:String , times:Long) :Long{

        if(dmy.isNullOrBlank()){
            return 1000L
        }
        when(dmy){
            getString(R.string.day)->{
                //1000 -> 1 second
                return (1000*60*60*24 / times)
            }
            getString(R.string.month)->{
               return (1000*60*60*24*30 / times)
            }
            getString(R.string.year)->{
                return (1000*60*60*24*365 / times)
            }
        }

        return 1000L
    }

    private fun stopnotificationforelement(id:String,context:Context , items:historyitem){
        //private fun scheduleNotification(interval:Long , Name:String , left:String , nstatus: Boolean , type:String , amount:String ,items:historyitem)
        val intent = Intent(applicationContext, Notification::class.java)
        val item = items
        val title = items.Title
        val message = items.desc


        Log.d("StopNotificaiton","receivedItem ${item}")
    //create same intent as the one passed

        intent.putExtra(parceltitle, item.Title)
        intent.putExtra(parceldesc, item.desc)
        intent.putExtra(parceldate, item.date)
        intent.putExtra(parceldose, item.dose)
        intent.putExtra(parceltype, item.type)
        intent.putExtra(parcelid, item.ID)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        // intent.putExtra(notificationID,item.ID.toInt());
        intent.putExtra(notificationStatus,item.nstatus);



      val notificationManager  =context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(id.toInt())


        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent =
            PendingIntent.getService(context, id.toInt(), intent,
                PendingIntent.FLAG_IMMUTABLE )


        val intent2 = Intent(applicationContext, Notification::class.java)
        val pendingIntent2 =
            PendingIntent.getService(context, id.toInt(), intent2,
                PendingIntent.FLAG_NO_CREATE )







            //PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
       // Toast.makeText(this, "Pending Intent"+pendingIntent.toString(), Toast.LENGTH_SHORT).show();

        Log.d("Stopping Pendingintent:","Intent:"+pendingIntent)
        Log.d("Stopping Pendingintent:","Intent:"+pendingIntent2)

        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent)
            Log.d("Alarm Cancelled ","ID :${id.toInt()}")
        }



    }

    private fun scheduleNotification(interval:Long , Name:String , left:String , nstatus: Boolean , type:String , amount:String ,items:historyitem)
    {
        createNotificationChannel()
        createLowPriorityNotificationChannel()

        val intent = Intent(applicationContext, Notification::class.java)

        val title = Name
        val message = "Time To Take $amount $type of your $Name Medication "
        val details = "Taken $amount $type of your $Name Medication "
        val item = items

        Log.d("AddEntry","receivedItem ${item}")



        intent.putExtra(parceltitle, item.Title)
        intent.putExtra(parceldesc, item.desc)
        intent.putExtra(parceldate, item.date)
        intent.putExtra(parceldose, item.dose)
        intent.putExtra(parceltype, item.type)
        intent.putExtra(parcelid, item.ID)
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        val rand = Random.Default
        var notificationuid = rand.nextInt(99999-10000)+10000
       // intent.putExtra(notificationID,item.ID.toInt());
        intent.putExtra(notificationStatus,item.nstatus);

        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            item.ID.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        Log.d("Creating Pendingintent:","Intent:"+pendingIntent)
        Log.d("Creating Notifciation for  ","ID :${item.ID.toInt()}")
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val time = getTime()
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,time ,interval,pendingIntent)


        showAlert(time, title, message+item.ID)
    }

    private fun showAlert(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(applicationContext)

        AlertDialog.Builder(this)
            .setTitle("Medication Notification Scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_ ->
                finish()
            }
            .show()

    }



    private fun getTime(): Long
    {
        val second = LocalDateTime.now().second
        val minute = LocalDateTime.now().minute
        val hour = LocalDateTime.now().hour
        val day = LocalDateTime.now().dayOfMonth
        val month = LocalDateTime.now().monthValue
        val year = LocalDateTime.now().year

        val calendar = Calendar.getInstance()
        //calendar.set(year, month, day, hour, minute ,second)
        Log.d("MSG","USED TIME"+second.toString())


        return calendar.timeInMillis + 1000


    }


    private fun createNotificationChannel()
    {
        val name = "My Medication tracker"
        val desc = "This is my medication tracker Notification Channel"
        var importance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }



    private fun createLowPriorityNotificationChannel()
    {
        val name = "My Medication tracker Low Priority"
        val desc = "This is my medication tracker Low Priority Notification Channel"
        var importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(channelID2, name, importance)
        channel.description = desc
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }





}//main
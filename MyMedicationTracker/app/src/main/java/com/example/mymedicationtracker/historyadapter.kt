package com.example.mymedicationtracker

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class historyadapter(public val dataSet: ArrayList<historyitem>) :
    RecyclerView.Adapter<historyadapter.newViewHolder>() {


    lateinit var personArrayList: ArrayList<historyitem>
    lateinit var list:ArrayList<historyitem>


    inner class newViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        lateinit var titletext: TextView
        lateinit var description: TextView
        lateinit var historydate: TextView

        init {
            // Define click listener for the ViewHolder's View.
            titletext = view.findViewById<TextView>(R.id.htitle_textview)
            description = view.findViewById<TextView>(R.id.hdescription_textview)

        }


    }



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): newViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.historycard, viewGroup, false)
        // add the 3 lines of code below to show 5 recycler items in the activity at a time
        val lp = view.getLayoutParams()
        view.setLayoutParams(lp)
        lp.height = 256
        return newViewHolder(view,viewGroup.context)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount():Int {
        return dataSet.size

    }
    // Replace the contents of a view (invoked by the layout manager)

    override fun onBindViewHolder(holder: newViewHolder, position: Int) {


        Log.d("DATE IS ","${dataSet.get(position).date}")
        val calendar: Calendar = Calendar.getInstance()

        calendar.set(dataSet.get(position).date.day, dataSet.get(position).date.month+1, dataSet.get(position).date.year)
        val day:kotlin.String = SimpleDateFormat("EEEE", Locale.ENGLISH).format(dataSet.get(position).date.time)
        val month:kotlin.String = SimpleDateFormat("MMMM", Locale.ENGLISH).format(dataSet.get(position).date.time)
        val year:Int = dataSet.get(position).date.year+1900

        val dateFormat = SimpleDateFormat("HH:mm:ss")
        val now = dataSet.get(position).date
        val formattedTime = dateFormat.format(now)



        holder.titletext.setText(dataSet.get(position).Title)

        holder.description.setText("${dataSet.get(position).dose} X ${dataSet.get(position).type}  ${day}/${month}/${year} $formattedTime")
       // holder.historydate.setText("${calendar.time.day} / ${calendar.time.month} / ${calendar.time.year+1900} ")







    }




}
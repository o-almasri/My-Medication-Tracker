package com.example.mymedicationtracker

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class settingsadapter(public val dataSet: ArrayList<Date>) :
    RecyclerView.Adapter<settingsadapter.newViewHolder>() {


    lateinit var personArrayList: ArrayList<Date>
    lateinit var list:ArrayList<Date>


    inner class newViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        lateinit var titletext: TextView
        lateinit var description: TextView
        lateinit var remove: Button
        lateinit var builder: AlertDialog.Builder

        init {
            // Define click listener for the ViewHolder's View.
            titletext = view.findViewById<TextView>(R.id.title_textview)
            description = view.findViewById<TextView>(R.id.description_textview)
            remove = view.findViewById<Button>(R.id.remove)




            remove.setOnClickListener {
                //--------
                val gson = Gson()

                dataSet.removeAt(adapterPosition)

                //save the changes to the storage

                val pref = view.getContext().getSharedPreferences("Gson", Context.MODE_PRIVATE);
                val prefsEditor = pref.edit()

                //serialize dataSet
                var temp = gson.toJson(dataSet);
                prefsEditor.putString("datelist", temp)
                prefsEditor.apply()

                notifyItemRemoved(adapterPosition);



            }

        }


    }



    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): newViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.settingscard, viewGroup, false)
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

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(dataSet.get(position).day, dataSet.get(position).month+1, dataSet.get(position).year)

        val day:kotlin.String = SimpleDateFormat("EEEE", Locale.ENGLISH).format(dataSet.get(position).time)
        val month:kotlin.String = SimpleDateFormat("MMMM", Locale.ENGLISH).format(dataSet.get(position).time)
        val year:Int = dataSet.get(position).year+1900

        holder.titletext.setText(day)
        holder.description.setText(""+calendar.time.day+"/"+month+"/"+ year)





    }


}
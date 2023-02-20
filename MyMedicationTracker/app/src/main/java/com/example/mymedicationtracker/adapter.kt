package com.example.mymedicationtracker

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.lang.String

class adapter(public val dataSet: ArrayList<entry>) :
    RecyclerView.Adapter<adapter.ViewHolder>() {

    lateinit var personArrayList: ArrayList<entry>
    lateinit var context: Context


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var titletext: TextView
        lateinit var desctext: TextView
        lateinit var editbtn: Button
        lateinit var builder:AlertDialog.Builder

        init {
            // Define click listener for the ViewHolder's View.
            titletext = view.findViewById<TextView>(R.id.titletxt)
            desctext = view.findViewById<TextView>(R.id.datetxt)
            editbtn = view.findViewById(R.id.editbtn);
            editbtn.setOnClickListener {


                //----------------------------------

                builder = AlertDialog.Builder(it.context)
                builder.setTitle("Edit Record")
                    .setMessage("Choose an action from below ")
                    .setCancelable(true)
                    .setPositiveButton("Edit"){dialogInterface,It->
                        dialogInterface.cancel()
                    }
                    .setNegativeButton("DELETE"){dialogInterface,It->
                        dataSet.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition);

                        //save the changes to the storage
                        val pref = view.getContext().getSharedPreferences("Gson", Context.MODE_PRIVATE);
                        val prefsEditor = pref.edit()
                        val gson = Gson()

                        //serialize dataSet
                        var temp = gson.toJson(dataSet);
                        prefsEditor.putString("list", temp)
                        prefsEditor.apply()
                    }
                        
                    .show()
            }







        }
    }





    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card, viewGroup, false)

        // add the 3 lines of code below to show 5 recycler items in the activity at a time
        val lp = view.getLayoutParams()
        view.setLayoutParams(lp)
        lp.height = 256
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // viewHolder.textViewMonthNum.setText(String.valueOf(dataSet.get(position).imonth))
        //  viewHolder.textViewMonth.setText(dataSet.get(position).smonth)

        viewHolder.titletext.setText(String.valueOf(dataSet.get(position).name))
        var word  =" times ";
        if(dataSet.get(position).times == 1){
            word  = " time "
        }
        viewHolder.desctext.setText(String.valueOf(dataSet.get(position).times)+word+String.valueOf(dataSet.get(position).dmy))
        viewHolder.editbtn.setText("Edit")


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount():Int {
        return dataSet.size
        //return 7;
    }



}
package com.example.mymedicationtracker

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.lang.String



class newadapter(public val dataSet: ArrayList<entry>) :
    RecyclerView.Adapter<newadapter.newViewHolder>() {

    lateinit var personArrayList: ArrayList<entry>

    lateinit var list:ArrayList<entry>
    lateinit var notificationManager: NotificationManager

    inner class newViewHolder(view: View , context: Context) : RecyclerView.ViewHolder(view) {
        lateinit var titletext: TextView
        lateinit var progressTxt: TextView
        lateinit var progressBar: ProgressBar
        lateinit var editbtn: Button
        lateinit var deletebtn: Button

        lateinit var builder: AlertDialog.Builder

        init {
            // Define click listener for the ViewHolder's View.
            titletext = view.findViewById<TextView>(R.id.Name)
            progressTxt = view.findViewById<TextView>(R.id.progresstxt)
            editbtn = view.findViewById(R.id.modifybtn);
            deletebtn = view.findViewById(R.id.deletebtn);
            progressBar = view.findViewById(R.id.progressBar);
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            editbtn.setOnClickListener {


                val gson = Gson()


                //generate a new empty list
                list = arrayListOf<entry>();
                //add item with same attributes to list
                list.add(dataSet[adapterPosition])

                //convert list to JSON
                var temp = gson.toJson(list);
                //set intent mode to Edit and content to JSON string
                val intent = Intent(it.context, Add_entry::class.java);
                intent.putExtra("mode", "edit")
                intent.putExtra("content", temp)

                //remove old item
                dataSet.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition);

                //save the changes to the storage
                val pref = view.getContext().getSharedPreferences("Gson", Context.MODE_PRIVATE);
                val prefsEditor = pref.edit()

                //serialize dataSet
                var temp1 = gson.toJson(dataSet);
                prefsEditor.putString("list", temp1)
                prefsEditor.apply()


                //start new activity
                it.context.startActivity(intent)


            }







            deletebtn.setOnClickListener {

                val gson = Gson()
                builder = AlertDialog.Builder(it.context)
                builder.setTitle("Delete Record")
                    .setMessage("You Are about to Delete this Record , Are you Sure")
                    .setCancelable(true)
                    .setNegativeButton("Cancel"){dialogInterface,it->
                        builder.show().dismiss()
                    }
                    .setNeutralButton("Stop Notification",{dialogInterface,It->
                        stopnotificationforelement(dataSet[adapterPosition].myID)
                    })
                    .setPositiveButton("DELETE"){dialogInterface,It->

                        stopnotificationforelement(dataSet[adapterPosition].myID)
                        dataSet.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition);

                        //save the changes to the storage
                        val pref = view.getContext().getSharedPreferences("Gson", Context.MODE_PRIVATE);
                        val prefsEditor = pref.edit()




                        //serialize dataSet
                        var temp = gson.toJson(dataSet);
                        prefsEditor.putString("list", temp)
                        prefsEditor.apply()
                    }

                    .show()

            }


        }


    }




    private fun stopnotificationforelement(id: kotlin.String){
        notificationManager.cancel(id.toInt())

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): newViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_new, viewGroup, false)
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

        holder.titletext.setText(String.valueOf(dataSet.get(position).name))




        val prog = (dataSet.get(position).current.toString().toFloat()/dataSet.get(position).tdoes.toString().toFloat() )*100
        if(prog>=100){
            holder.progressTxt.setText("Completed")
        }else {
            holder.progressTxt.setText(dataSet.get(position).current.toString()+"/"+dataSet.get(position).tdoes.toString())
        }
        holder.progressBar.setProgress(prog.toInt())

    }


}
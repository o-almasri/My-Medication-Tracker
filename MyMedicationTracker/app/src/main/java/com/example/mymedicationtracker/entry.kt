package com.example.mymedicationtracker

data class entry(
    //name of the medication
    val name: String,
    //how many times per dmy it should be taken
    val times: Int,
    //day or month or year representation
    val dmy:String,
    // startdate for medication
    val startdate:String,
    // if medication is completed
    var completed:Boolean,
    // total doses
    var tdoes:Int,
    // current progress
    var current:Int,
    //unique ID for notifications
    var myID : String

)

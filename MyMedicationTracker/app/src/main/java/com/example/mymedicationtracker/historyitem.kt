package com.example.mymedicationtracker

import android.os.Parcel
import android.os.Parcelable
import java.util.Date

data class historyitem(
    val Title : String,
    val desc  :String,
    val date: Date,
    val dose:String ,
    val type:String ,
    val ID:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        Date(parcel.readLong()),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Title)
        parcel.writeString(desc)
        parcel.writeLong(date.time)
        parcel.writeString(dose)
        parcel.writeString(type)
        parcel.writeString(ID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<historyitem> {
        override fun createFromParcel(parcel: Parcel): historyitem {
            return historyitem(parcel)
        }

        override fun newArray(size: Int): Array<historyitem?> {
            return arrayOfNulls(size)
        }
    }
}


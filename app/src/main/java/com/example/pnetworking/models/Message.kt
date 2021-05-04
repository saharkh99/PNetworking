package com.example.pnetworking.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Message(
    val id: String = "",
    val idUSer: String = "",
    val context: String = "",
    val idTo: String = "",
    val timestamp: Long,//string in format yy/mm/dd:mm
    val type: String = "text",
    val imageUrl: String = "",
    val isSeen:Boolean=false
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readBoolean()!!
    )

    constructor() : this("", "", "", "", 0, "", "",false)

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idUSer)
        parcel.writeString(context)
        parcel.writeString(idTo)
        parcel.writeString(id)
        parcel.writeLong(timestamp)
        parcel.writeString(type)
        parcel.writeString(imageUrl)
        parcel.writeBoolean(isSeen)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Message> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}
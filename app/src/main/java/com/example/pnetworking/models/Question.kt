package com.example.pnetworking.models

import android.os.Parcel
import android.os.Parcelable

data class Question(
    var title: String = "",
    var score: Int = 0,
    var type: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()!!,
        parcel.readString()!!

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(type)
        parcel.writeInt(score)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Question> {
        override fun createFromParcel(parcel: Parcel): Question {
            return Question(parcel)
        }

        override fun newArray(size: Int): Array<Question?> {
            return arrayOfNulls(size)
        }
    }
}
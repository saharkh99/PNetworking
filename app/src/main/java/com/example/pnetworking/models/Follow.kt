package com.example.pnetworking.models

import android.os.Parcel
import android.os.Parcelable

data class Follow(
    var idUSer: String = "",
    var idFriend: String = "",
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(idUSer)
        parcel.writeString(idFriend)
    }

    override fun describeContents()=0

    companion object CREATOR : Parcelable.Creator<Follow> {
        override fun createFromParcel(parcel: Parcel): Follow {
            return Follow(parcel)
        }

        override fun newArray(size: Int): Array<Follow?> {
            return arrayOfNulls(size)
        }
    }
}
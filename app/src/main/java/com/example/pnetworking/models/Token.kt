package com.example.pnetworking.models

import android.os.Parcel
import android.os.Parcelable

data class Token(var token: String = ""):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(token)
    }

    override fun describeContents()=0

    companion object CREATOR : Parcelable.Creator<Token> {
        override fun createFromParcel(parcel: Parcel): Token {
            return Token(parcel)
        }

        override fun newArray(size: Int): Array<Token?> {
            return arrayOfNulls(size)
        }
    }
}
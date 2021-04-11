package com.example.pnetworking.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

class User(
    var id: String ,
    var emailText:String,
    var name:String,
    var imageProfile:String,
    var connection:Int,
    var isTypingTo:String,
    var birthday:String,
    var gender:String,
    var online:Boolean,
    var score:String,
    var favorites:String,


): Parcelable {
     @RequiresApi(Build.VERSION_CODES.Q)
     constructor(parcel: Parcel) : this(
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readInt(),
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readBoolean(),
         parcel.readString()!!,
         parcel.readString()!!,

         ) {
     }

     constructor() : this("","", "", "",0,"","","",false,"0","")

     override fun describeContents()=0

     @RequiresApi(Build.VERSION_CODES.Q)
     override fun writeToParcel(dest: Parcel, flags: Int)= with(dest) {
         writeString(id)
         writeString(emailText)
         writeString(name)
         writeString(imageProfile)
         writeInt(connection)
         writeString(isTypingTo)
         writeString(birthday)
         writeString(gender)
         writeBoolean(online)
         writeString(score)
         writeString(favorites)

     }

     companion object CREATOR : Parcelable.Creator<User> {
         @RequiresApi(Build.VERSION_CODES.Q)
         override fun createFromParcel(parcel: Parcel): User {
             return User(parcel)
         }

         override fun newArray(size: Int): Array<User?> {
             return arrayOfNulls(size)
         }
     }
 }

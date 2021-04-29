package com.example.pnetworking.models

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

class User(
    var birthday:String,
    var favorites:String,
    var score:String,
    var gender:String,
    var typingTo:String,
    var name:String,
    var bio:String,
    var imageProfile:String,
    var online:Boolean,
    var connection:Int,
    var emailText:String,
    var id: String ,

): Parcelable {
     @RequiresApi(Build.VERSION_CODES.Q)
     constructor(parcel: Parcel) : this(
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readString()!!,
         parcel.readBoolean(),
         parcel.readInt(),
         parcel.readString()!!,
         parcel.readString()!!,

         ) {
     }

     constructor() : this("","","", "", "","","","",false,0,"0","")

     override fun describeContents()=0

     @RequiresApi(Build.VERSION_CODES.Q)
     override fun writeToParcel(dest: Parcel, flags: Int)= with(dest) {
         writeString(id)
         writeString(emailText)
         writeString(name)
         writeString(imageProfile)
         writeInt(connection)
         writeString(typingTo)
         writeString(birthday)
         writeString(gender)
         writeValue(online)
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

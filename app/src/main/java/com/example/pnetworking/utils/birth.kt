package com.example.pnetworking.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*

var zodiacSign : String=""
fun zodiac(birth: String):String {
    val str = birth.split("/".toRegex()).toTypedArray()
    val day = str[1].toInt()
    Log.d("day",day.toString())
    val month = str[0].toInt()
    when (month) {
        1 -> {

            if (day < 20) {
                zodiacSign = "Capricorn"
            } else {
                zodiacSign = "Aquarius"
            }

        }
        2 ->
            if (day < 18) {
                zodiacSign = "Aquarius"
            } else {
                zodiacSign = "Pisces"
            }
        3 ->
            if (day < 21) {
                zodiacSign = "Pisces"
            } else {
                zodiacSign = "Aries"
            }
        4 ->
            if (day < 20) {
                zodiacSign = "Aries"
            } else {
                zodiacSign = "Taurus"
            }
        5 ->
            if (day < 21) {
                zodiacSign = "Taurus"
            } else {
                zodiacSign = "Gemini"
            }
        6 ->
            if (day < 21) {
                zodiacSign = "Gemini"
            } else {
                zodiacSign = "Cancer"
            }
        7 ->
            if (day < 23) {
                zodiacSign = "Cancer"
            } else {
                zodiacSign = "Leo"
            }
        8 ->
            if (day < 23) {
                zodiacSign = "Leo"
            } else {
                zodiacSign = "Virgo"
            }
        9 ->
            if (day < 23) {
                zodiacSign = "Virgo"
            } else {
                zodiacSign = "Libra"
            }
        10 ->
            if (day < 23) {
                zodiacSign = "Libra"
            } else {
                zodiacSign = "Scorpio"
            }
        11 ->
            if (day < 22) {
                zodiacSign = "Scorpio"
            } else {
                zodiacSign = "Sagittarius"
            }
        12 ->
            if (day < 22) {
                zodiacSign = "Sagittarius"
            } else {
                zodiacSign = "Capricorn"
            }
    }
    return zodiacSign
}

@RequiresApi(Build.VERSION_CODES.O)
fun findAge(birth: String):Int{
    val str = birth.split("/".toRegex()).toTypedArray()

    val year = str[2].toInt()
    Log.d("year",year.toString())
    Log.d("year",str[2])
    val today: Calendar = Calendar.getInstance()
    val age=today.get(Calendar.YEAR)-year
    return age
}

package com.hao.horderfood

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private var sharedPreferences: SharedPreferences? = null
    private const val SHARED_NAME = "STORE"
    private const val SHARED_USER = "TYPE"

    fun getInstance(context: Context?): SharedPreferences? {
        if (sharedPreferences == null) {
            sharedPreferences = context!!.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        }
        return sharedPreferences
    }

    fun getNameUser(context: Context?): String? {
        return getInstance(context)!!.getString(SHARED_USER, "ABC")
    }

    fun setNameUser(context: Context?, unit: String) {
        getInstance(context)!!.edit().putString(SHARED_USER, unit).commit()
    }

}
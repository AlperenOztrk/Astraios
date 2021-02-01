package com.wiuma.astraios.utilities

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager

class PrivateSharedPreferences {
    companion object {
        private var LAST_TIME = "lastTime"
        private var sharedPreferences: SharedPreferences? = null

        @Volatile
        private var instance: PrivateSharedPreferences? = null
        private var lock = Any()
        operator fun invoke(context: Context): PrivateSharedPreferences = instance
                ?: synchronized(lock) {
                    instance ?: createPrivateSharedPreferences(context).also {
                        instance = it
                    }
                }

        private fun createPrivateSharedPreferences(context: Context): PrivateSharedPreferences {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            return PrivateSharedPreferences()
        }
    }

    fun saveTime(time: Long) {
        sharedPreferences?.edit(commit = true) {
            putLong(LAST_TIME, time)
        }
    }

    fun getTime() = sharedPreferences?.getLong(LAST_TIME, 0)
}
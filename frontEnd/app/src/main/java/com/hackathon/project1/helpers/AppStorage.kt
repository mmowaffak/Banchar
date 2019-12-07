package com.hackathon.project1.helpers

import android.content.Context
import com.securepreferences.SecurePreferences

object AppStorage {

    private var prefs: SecurePreferences? = null

    fun initialize(context: Context) {
        prefs = SecurePreferences(context, "", "prefs.xml")
        SecurePreferences.setLoggingEnabled(true)
    }


    var jwtToken: String?
        get() = prefs?.getString("JWT", null)
        set(value) = prefs?.edit()?.putString("JWT", value)!!.apply()

    var userId: Int?
        get() = prefs?.getInt("ID", 0)
        set(value) = value?.let { prefs?.edit()?.putInt("ID", it) }!!.apply()


}
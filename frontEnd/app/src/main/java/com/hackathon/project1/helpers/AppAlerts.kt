package com.hackathon.project1.helpers

import android.content.Context
import android.widget.Toast

object AppAlerts {

    fun missingInput(context: Context?) {
        context?.let { Toast.makeText(it,"Please enter the missing fields", Toast.LENGTH_LONG).show() }
    }

    fun passwordNotMatchInput(context: Context?) {
        context?.let { Toast.makeText(it,"The confirmation Password doesn't match the password", Toast.LENGTH_LONG).show() }
    }


    fun showError(context: Context?, msg: String?) {
        context?.let { Toast.makeText(it,msg, Toast.LENGTH_LONG).show() }

    }
}
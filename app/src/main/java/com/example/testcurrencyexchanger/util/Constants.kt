package com.example.testcurrencyexchanger.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface


object Constants {

    const val BASE_URL = "https://developers.paysera.com"
    const val FEE = 0.007
    const val ZERO_FEE = 0.0

    fun dialogString(context: Context, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.setCancelable(true)
        builder.setPositiveButton(
            "Ok"
        ) { dialog, _ -> dialog.cancel() }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
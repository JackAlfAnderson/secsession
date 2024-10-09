package com.example.secondsessionprof.data.emailSave

import android.content.Context
import androidx.core.content.edit

class EmailManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("str", Context.MODE_PRIVATE)
    fun set(email: String){
        sharedPreferences.edit{
            putString("email", email)
        }
    }
    fun get(): String{
        val email = sharedPreferences.getString("email", "")
        return email.toString()
    }

}
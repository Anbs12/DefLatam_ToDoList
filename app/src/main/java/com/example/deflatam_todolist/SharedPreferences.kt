package com.example.deflatam_todolist

import android.content.Context

class SharedPreferences(context: Context ) {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
}
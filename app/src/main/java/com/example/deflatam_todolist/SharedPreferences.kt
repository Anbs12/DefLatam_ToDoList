package com.example.deflatam_todolist

import android.content.Context

class SharedPreferences(context: Context ) {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveList(list: List<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet("tareas", list.toSet())
        editor.apply()
    }

    fun getList(): List<String> {
        return sharedPreferences.getStringSet("tareas", emptySet())?.toList() ?: emptyList()
    }
}
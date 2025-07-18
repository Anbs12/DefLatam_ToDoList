package com.example.deflatam_todolist.model

/** Propiedades de cada tarea.*/
data class Tarea(
    val id: Long,
    val titulo: String,
    var descripcion: String? = null,
    var isCompletada: Boolean,
    val fechaCreacion: Long = System.currentTimeMillis()
)

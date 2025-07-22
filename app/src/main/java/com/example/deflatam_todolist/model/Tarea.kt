package com.example.deflatam_todolist.model

import kotlin.random.Random

const val MIN_VALUE = 1
const val MAX_VALUE = 1000000000

/** Propiedades de cada tarea.*/
data class Tarea(
    val id: Int = Random.nextInt(MIN_VALUE, MAX_VALUE),
    var descripcion: String? = null,
    var isCompletada: Boolean = false,
    val fechaCreacion: Long = System.currentTimeMillis()
)

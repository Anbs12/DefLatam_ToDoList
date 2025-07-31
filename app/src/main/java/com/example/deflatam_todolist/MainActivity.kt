package com.example.deflatam_todolist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deflatam_todolist.adapter.TareasAdapter
import com.example.deflatam_todolist.model.Tarea
import com.example.deflatam_todolist.utils.SwipeToDeleteCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var btnAgregarTarea: Button
    private lateinit var edtxtTarea: EditText
    private lateinit var txtTareasPendientes: TextView
    private lateinit var recyclerView: RecyclerView

    private lateinit var btnFiltroPendienteDialog: Button
    private lateinit var btnFiltroCompletadasDialog: Button

    private var listaDeTareas = mutableListOf<Tarea>()

    /** Serializa las tareas.*/
    private val gson = Gson()

    private val tareasAdapter by lazy {
        TareasAdapter(listaDeTareas, { id, isCompletada ->
            listaDeTareas.forEach { tarea ->
                if (tarea.id == id) {
                    tarea.isCompletada = isCompletada
                }
            }
            guardarTareas()
            updateTareasPendientesNumberUi()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponents()
        initRecyclerView()
        cargarTareasGuardadas()
    }

    override fun onPause() {
        super.onPause()
        // Guarda las tareas pendientes antes de salir de la actividad
        guardarTareas()
    }

    private fun initComponents() {
        edtxtTarea = findViewById(R.id.input_ingreseTarea)
        txtTareasPendientes = findViewById(R.id.txtTareasPendientes)
        initListeners()
    }

    private fun initListeners() {
        btnAgregarTarea = findViewById(R.id.btnAgregarTarea)
        btnAgregarTarea.setOnClickListener {
            agregarNuevaTarea()
        }
        btnFiltroPendienteDialog = findViewById(R.id.btnFiltroPendientes)
        btnFiltroPendienteDialog.setOnClickListener {
            filtrarTareasPendientesDialog()
        }
        btnFiltroCompletadasDialog = findViewById(R.id.btnFiltroCompletadas)
        btnFiltroCompletadasDialog.setOnClickListener {
            filtrarTareasCompletadasDialog()
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView_tareas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tareasAdapter
        swipeHandler()
    }

    /** Elimina tarea con swipe.*/
    private fun swipeHandler() {
        val swipeHandler = SwipeToDeleteCallback { pos ->
            tareasAdapter.eliminarTarea(pos)
            //listaDeTareas.removeAt(pos)
            updateTareasPendientesNumberUi()
            guardarTareas()
            sms("Tarea eliminada")
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)
    }

    private fun agregarNuevaTarea() {
        val descripcion = edtxtTarea.text.toString()
        if (descripcion.isNotEmpty()) {
            val nuevaTarea = Tarea(descripcion = descripcion)
            //listaDeTareas.add(0, nuevaTarea)
            tareasAdapter.agregarTarea(nuevaTarea)
            edtxtTarea.text.clear()
            updateTareasPendientesNumberUi()
            guardarTareas()
            recyclerView.scrollToPosition(0)
        } else {
            Toast.makeText(this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
        }
    }

    /** Guarda las tareas.*/
    private fun guardarTareas() {
        val sharedPreferences = getSharedPreferences("ToDoListPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val jsonPendientes = gson.toJson(listaDeTareas)
        editor.putString("lista_tareas_pendientes", jsonPendientes)
        editor.apply()
    }

    /** Carga las tareas guardadas.*/
    private fun cargarTareasGuardadas() {
        val sharedPreferences = getSharedPreferences("ToDoListPrefs", MODE_PRIVATE)
        val jsonPendientes = sharedPreferences.getString("lista_tareas_pendientes", null)
        if (jsonPendientes != null) {
            val type = object : TypeToken<MutableList<Tarea>>() {}.type
            val tareasGuardadas: MutableList<Tarea> = gson.fromJson(jsonPendientes, type)
            listaDeTareas.addAll(tareasGuardadas)
        }
        updateTareasPendientesNumberUi()
    }

    /** Actualiza el numero de tareas pendientes.*/
    @SuppressLint("SetTextI18n")
    private fun updateTareasPendientesNumberUi() {
        val tareasPendientes = listaDeTareas.filter { !it.isCompletada }
        if (tareasPendientes.isEmpty()) {
            txtTareasPendientes.visibility = View.GONE
        } else {
            txtTareasPendientes.visibility = View.VISIBLE
            txtTareasPendientes.text = "Tienes ${tareasPendientes.size} listaDeTareas pendientes."
        }
    }

    private fun sms(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun filtrarTareasCompletadasDialog(){
        val tareasCompletadas = listaDeTareas.filter { it.isCompletada }
        val dialogFragment = TareasListDialogFragment(tareasCompletadas, "Completadas")
        dialogFragment.show(supportFragmentManager, "TareasCompletadasDialogFragment")
    }

    private fun filtrarTareasPendientesDialog() {
        val tareasPendientes = listaDeTareas.filter { !it.isCompletada }
        val dialogFragment = TareasListDialogFragment(tareasPendientes, "Pendientes")
        dialogFragment.show(supportFragmentManager, "TareasCompletadasDialogFragment")
    }

}



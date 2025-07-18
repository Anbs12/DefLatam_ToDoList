package com.example.deflatam_todolist

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deflatam_todolist.adapter.TareasAdapter
import com.example.deflatam_todolist.model.Tarea
import com.example.deflatam_todolist.utils.SwipeToDeleteCallback

class MainActivity : AppCompatActivity() {

    private lateinit var btnAgregarTarea: Button
    private lateinit var edtxtTarea: EditText
    private lateinit var txtTareasPendientes: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tareasAdapter: TareasAdapter

    private lateinit var checkBoxTareasPendientes: RadioButton
    private lateinit var checkBoxTareasCompletadas: RadioButton
    private lateinit var checkBoxTodasTareas: RadioButton

    private var tareas = mutableListOf<Tarea>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initComponents()
        initButton()
        initRecyclerView()
        getTareasPendientes()
        checkBoxFunctionality()
    }

    private fun initComponents() {
        edtxtTarea = findViewById(R.id.input_ingreseTarea)
        txtTareasPendientes = findViewById(R.id.txtTareasPendientes)
        checkBoxTareasPendientes = findViewById(R.id.checkbox_tarea_pendiente)
        checkBoxTareasCompletadas = findViewById(R.id.checkbox_tarea_completada)
        checkBoxTodasTareas = findViewById(R.id.checkbox_tarea_todo)
    }

    private fun initButton() {
        btnAgregarTarea = findViewById(R.id.btnAgregarTarea)
        btnAgregarTarea.setOnClickListener {
            when {
                edtxtTarea.text.isEmpty() -> {
                    sms("Ingrese una tarea")
                    return@setOnClickListener
                }

                edtxtTarea.text.length > 151 -> {
                    sms("La tarea no puede tener mas de 150 caracteres")
                    return@setOnClickListener
                }

                edtxtTarea.text.length < 3 -> {
                    sms("La tarea debe tener al menos 3 caracteres")
                    return@setOnClickListener
                }
            }
            addNewTareaToRecyclerView()
            getTareasPendientes()
            edtxtTarea.editableText.clear()
        }
    }

    private fun initRecyclerView() {
        tareasAdapter = TareasAdapter(tareas) {
            getTareasPendientes()
        }
        recyclerView = findViewById(R.id.recyclerView_tareas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = tareasAdapter
        swipeHandler()
    }

    private fun swipeHandler() {
        val swipeHandler = SwipeToDeleteCallback { pos ->
            tareasAdapter.eliminarTarea(pos)
            getTareasPendientes()
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)
    }

    private fun addNewTareaToRecyclerView() {
        val descripcion = edtxtTarea.text.toString()
        val conteoTittleId = tareas.size + 1
        tareasAdapter.agregarTarea(
            Tarea(
                id = conteoTittleId.toLong(),
                titulo = "Tarea $conteoTittleId",
                descripcion = descripcion,
                isCompletada = false
            )
        )
    }

    private fun getTareasPendientes() {
        val tareasPendientes = tareas.filter { !it.isCompletada }
        if (tareasPendientes.isEmpty()) {
            txtTareasPendientes.visibility = View.GONE
        } else {
            txtTareasPendientes.visibility = View.VISIBLE
            txtTareasPendientes.text = "Tienes ${tareasPendientes.size} tareas pendientes."
        }
    }

    private fun checkBoxFunctionality() {
        checkBoxTodasTareas.isChecked = true
        checkBoxTareasPendientes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tareasAdapter.obtenerTareasPendientes()
            }
            getTareasPendientes()
        }
        checkBoxTareasCompletadas.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tareasAdapter.obtenerTareasCompletadas()
            }
            getTareasPendientes()
        }
        checkBoxTodasTareas.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tareasAdapter.reestablecerTareas()
            }
            getTareasPendientes()
        }
    }

    private fun sms(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}
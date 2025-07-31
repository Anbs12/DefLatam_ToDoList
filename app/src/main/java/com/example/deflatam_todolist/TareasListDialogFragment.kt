package com.example.deflatam_todolist

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deflatam_todolist.adapter.TareasDialogAdapter
import com.example.deflatam_todolist.model.Tarea

class TareasListDialogFragment(private val taskList: List<Tarea>, private val textoFilter: String) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_tareas_list, null)

        val recyclerViewDialogTasks = view.findViewById<RecyclerView>(R.id.recyclerView_dialog_tareas)
        recyclerViewDialogTasks.layoutManager = LinearLayoutManager(requireContext())

        val adapter =
            TareasDialogAdapter(taskList.toMutableList()) // Pasa una copia de la lista
        recyclerViewDialogTasks.adapter = adapter

        builder.setView(view)
            .setTitle("Lista de Tareas $textoFilter")
            .setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }
}
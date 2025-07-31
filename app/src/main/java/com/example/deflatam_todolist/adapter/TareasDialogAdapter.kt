package com.example.deflatam_todolist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deflatam_todolist.R
import com.example.deflatam_todolist.model.Tarea

@SuppressLint("NotifyDataSetChanged")
class TareasDialogAdapter(
    private var listaDeTareas: MutableList<Tarea>
) :
    RecyclerView.Adapter<TareasDialogAdapter.TareaViewHolder>() {


    //Coloca variables aca
    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCompletada = itemView.findViewById<TextView>(R.id.txtCompletadaDialog)
        val txtDescripcion = itemView.findViewById<TextView>(R.id.txtDescripcionDialog)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkbox_tareaDialog)
    }


    //Infla el layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea_dialog, parent, false)
        return TareaViewHolder(view)
    }

    //Aqui en onBindViewHolder con el holder, asignamos los elementos que
    // necesitamos ver en el cardview
    //Y las funciones que creemos en el "ViewHolder"
    override fun onBindViewHolder(
        holder: TareaViewHolder,
        position: Int
    ) {
        val tarea = listaDeTareas[position]
        //Asignamos datos
        holder.txtDescripcion.text = tarea.descripcion
        holder.checkBox.isChecked = tarea.isCompletada
        holder.txtCompletada.visibility = if (tarea.isCompletada) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = listaDeTareas.size

}
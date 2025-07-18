package com.example.deflatam_todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deflatam_todolist.R
import com.example.deflatam_todolist.model.Tarea

class TareasAdapter(
    private var tareas: MutableList<Tarea>,
    private val onTareaChecked: (tarea: Tarea) -> Unit,
) :
    RecyclerView.Adapter<TareasAdapter.TareaViewHolder>() {

        private var tareasALmacenadas = mutableListOf<Tarea>()

    //Coloca variables aca
    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo = itemView.findViewById<TextView>(R.id.txtTitulo)
        val txtCompletada = itemView.findViewById<TextView>(R.id.txtCompletada)
        val txtDescripcion = itemView.findViewById<TextView>(R.id.txtDescripcion)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkbox_tarea)
        val btnEditarTarea = itemView.findViewById<TextView>(R.id.btnEditarTarea)
    }


    //Infla el layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    //Aqui en onBindViewHolder con el holder, asignamos los elementos que
    // necesitamos ver en el cardview
    //Y las funciones que creemos en el "ViewHolder"
    override fun onBindViewHolder(
        holder: TareaViewHolder,
        position: Int
    ) {
        val tarea = tareas[position]
        holder.titulo.text = tarea.titulo
        holder.txtDescripcion.text = tarea.descripcion
        holder.checkBox.isChecked = tarea.isCompletada
        holder.txtCompletada.visibility = View.GONE

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            tarea.isCompletada = isChecked
            if (tarea.isCompletada) {
                holder.txtCompletada.visibility = View.VISIBLE
            }else{
                holder.txtCompletada.visibility = View.GONE
            }
            onTareaChecked(tarea)
        }

        holder.btnEditarTarea.setOnClickListener {
        }
    }

    override fun getItemCount() = tareas.size

    fun eliminarTarea(pos: Int) {
        tareas.removeAt(pos)
        tareasALmacenadas.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun agregarTarea(tarea: Tarea) {
        tareas.add(0, tarea)
        tareasALmacenadas.add(0, tarea)
        notifyItemInserted(0)
    }

    fun obtenerTareasPendientes(){
        val tareasPendientes = tareas.filter { !it.isCompletada }
        tareas.clear()
        tareas.addAll(tareasPendientes)
        notifyDataSetChanged()
    }

    fun obtenerTareasCompletadas(){
        val tareasCompletadas = tareas.filter { it.isCompletada }
        tareas.clear()
        tareas.addAll(tareasCompletadas)
        notifyDataSetChanged()
    }

    fun reestablecerTareas(){
        tareas.clear()
        tareas.addAll(tareasALmacenadas)
        notifyDataSetChanged()
    }

    fun modificarTarea(position: Int,descripcion: String){
        tareas[position].descripcion = descripcion
        tareasALmacenadas[position].descripcion = descripcion
        notifyItemChanged(0)
    }

}
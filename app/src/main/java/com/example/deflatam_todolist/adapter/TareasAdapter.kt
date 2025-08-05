package com.example.deflatam_todolist.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.deflatam_todolist.R
import com.example.deflatam_todolist.model.Tarea
import com.google.android.material.textfield.TextInputLayout

@SuppressLint("NotifyDataSetChanged")
class TareasAdapter(
    private var listaDeTareas: MutableList<Tarea>,
    private val onTareaChecked: (Int, isCompletada: Boolean) -> Unit
) :
    RecyclerView.Adapter<TareasAdapter.TareaViewHolder>() {


    //Coloca variables aca
    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCompletada = itemView.findViewById<TextView>(R.id.txtCompletada)
        val txtDescripcion = itemView.findViewById<TextView>(R.id.txtDescripcion)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkbox_tarea)
        val btnEditarTarea = itemView.findViewById<Button>(R.id.btnEditarTarea)
        val txtDescripcionEditable = itemView.findViewById<EditText>(R.id.txtDescripcionEditable)
        val btnEditarTareaCancelar = itemView.findViewById<Button>(R.id.btnEditarTareaCancelar)
        val btnConfirmarEdicionTarea = itemView.findViewById<Button>(R.id.btnConfirmarEdicionTarea)
        val inputLayout_edText = itemView.findViewById<TextInputLayout>(R.id.input_layout_descripcionEditable)
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
        val tarea = listaDeTareas[position]
        //Asignamos datos
        holder.txtDescripcion.text = tarea.descripcion
        holder.checkBox.isChecked = tarea.isCompletada
        println("Fecha de este card: ${tarea.descripcion} " + tarea.fechaCreacion)

        //Operaciones de los listener
        holder.txtCompletada.visibility = if (tarea.isCompletada) View.VISIBLE else View.GONE
        holder.txtDescripcionEditable.setText(tarea.descripcion)
        holder.txtDescripcionEditable.visibility = View.GONE

        holder.inputLayout_edText.visibility = View.GONE

        //Checkbox de true o false de la tarea
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            tarea.isCompletada = isChecked
            //actualizarTareaCompletada(position, isChecked)
            onTareaChecked(tarea.id, isChecked)
            holder.txtCompletada.visibility = if (tarea.isCompletada) View.VISIBLE else View.GONE
        }

        //Edicion de tarea
        holder.btnEditarTarea.setOnClickListener {
            //Aparecen los objetos para editar
            holder.btnEditarTareaCancelar.visibility = View.VISIBLE
            holder.btnConfirmarEdicionTarea.visibility = View.VISIBLE
            holder.txtDescripcionEditable.visibility = View.VISIBLE
            holder.txtDescripcionEditable.setText("")
            holder.inputLayout_edText.visibility = View.VISIBLE

            //Desaparecen objetos que no se necesitan
            holder.txtDescripcion.visibility = View.GONE
            holder.btnEditarTarea.visibility = View.GONE
        }

        //Aparece al presionar el boton Editar
        holder.btnConfirmarEdicionTarea.setOnClickListener {
            //Desaparecen los objetos de edicion
            holder.txtDescripcionEditable.visibility = View.GONE
            holder.btnEditarTareaCancelar.visibility = View.GONE
            holder.btnConfirmarEdicionTarea.visibility = View.GONE
            holder.inputLayout_edText.visibility = View.GONE
            //Aparecen objetos con descripcion actualizada
            holder.txtDescripcion.visibility = View.VISIBLE
            holder.btnEditarTarea.visibility = View.VISIBLE

            //Actualizamos la descripcion
            val nuevaDescripcion = holder.txtDescripcionEditable.text.toString()
            modificarTarea(position, nuevaDescripcion)
            holder.txtDescripcion.text = nuevaDescripcion
        }

        //Aparece al presionar el boton Editar
        holder.btnEditarTareaCancelar.setOnClickListener {
            //Aparecen objetos con descripcion anterior
            holder.txtDescripcion.visibility = View.VISIBLE
            holder.btnEditarTarea.visibility = View.VISIBLE
            //Desaparecen objetos para edicion de tarea
            holder.btnConfirmarEdicionTarea.visibility = View.GONE
            holder.txtDescripcionEditable.visibility = View.GONE
            holder.btnEditarTareaCancelar.visibility = View.GONE
        }
    }

    override fun getItemCount() = listaDeTareas.size

    fun agregarTarea(tarea: Tarea) {
        listaDeTareas.add(0, tarea)
        notifyItemInserted(0)
    }

    fun eliminarTarea(position: Int) {
        listaDeTareas.removeAt(position)
        notifyItemRemoved(position)
    }

    fun modificarTarea(position: Int, descripcion: String) {
        listaDeTareas[position].descripcion = descripcion
        notifyItemRangeChanged(position, listaDeTareas.size)
    }

}
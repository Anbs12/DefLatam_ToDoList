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

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //Coloca variables de inicializacion de elementos XML acá ⬇️

        val txtCompletada = itemView.findViewById<TextView>(R.id.txtCompletada)
        val txtDescripcion = itemView.findViewById<TextView>(R.id.txtDescripcion)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkbox_tarea)
        val btnEditarTarea = itemView.findViewById<Button>(R.id.btnEditarTarea)
        val txtDescripcionEditable = itemView.findViewById<EditText>(R.id.txtDescripcionEditable)
        val btnEditarTareaCancelar = itemView.findViewById<Button>(R.id.btnEditarTareaCancelar)
        val btnConfirmarEdicionTarea = itemView.findViewById<Button>(R.id.btnConfirmarEdicionTarea)
        val inputLayout_edText =
            itemView.findViewById<TextInputLayout>(R.id.input_layout_descripcionEditable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        //Infla el layout
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TareaViewHolder,
        position: Int
    ) {
        /* Controla elementos del card actual */

        val tarea = listaDeTareas[position]

        //Asignamos datos
        holder.txtDescripcion.text = tarea.descripcion
        holder.checkBox.isChecked = tarea.isCompletada
        holder.txtDescripcionEditable.setText(tarea.descripcion)

        holder.txtCompletada.visibility = if (tarea.isCompletada) View.VISIBLE else View.GONE

        //Desaparece modo editar
        holder.txtDescripcionEditable.visibility = View.GONE
        holder.inputLayout_edText.visibility = View.GONE

        //-------- Operaciones de los listener ⬇️

        //Checkbox de true o false de la tarea
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            tarea.isCompletada = isChecked
            onTareaChecked(tarea.id, isChecked)
            holder.txtCompletada.visibility = if (tarea.isCompletada) View.VISIBLE else View.GONE
        }

        //Activa el modo para editar tarea.
        holder.btnEditarTarea.setOnClickListener {
            onEditarTareaSeleccionada(holder)
        }

        //Confirma cambios y actualiza tarea.
        holder.btnConfirmarEdicionTarea.setOnClickListener {
            onConfirmarEdicionTarea(holder, position)
        }

        //Cancela los cambios y mantiene valor original.
        holder.btnEditarTareaCancelar.setOnClickListener {
            onCancelarEdicionTarea(holder)
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

    private fun modificarTarea(position: Int, descripcion: String) {
        listaDeTareas[position].descripcion = descripcion
        notifyItemRangeChanged(position, listaDeTareas.size)
    }

    /**Activa el modo para editar una tarea seleccionada*/
    private fun onEditarTareaSeleccionada(holder: TareaViewHolder) {
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

    /**Confirma la edicion de la tarea seleccionada y actualiza los cambios*/
    private fun onConfirmarEdicionTarea(holder: TareaViewHolder, position: Int) {

        //Desaparecen objetos edicion
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

    /**Cancela los cambios de la tarea seleccionada y mantiene la descripcion original*/
    private fun onCancelarEdicionTarea(holder: TareaViewHolder) {

        //Aparecen objetos con descripcion anterior
        holder.txtDescripcion.visibility = View.VISIBLE
        holder.btnEditarTarea.visibility = View.VISIBLE

        //Desaparecen objetos para edicion de tarea
        holder.btnConfirmarEdicionTarea.visibility = View.GONE
        holder.txtDescripcionEditable.visibility = View.GONE
        holder.btnEditarTareaCancelar.visibility = View.GONE
    }

}
package com.example.proyecto_agenda_telefonica

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class TelefonoDetalleAdapter(
    private var telefonos: List<TelefonoResponse>,
    private val onUpdateClick: (TelefonoResponse) -> Unit,
    private val onDeleteClick: (TelefonoResponse) -> Unit
) : RecyclerView.Adapter<TelefonoDetalleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TelefonoDetalleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_telefono_detalle, parent, false)
        return TelefonoDetalleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TelefonoDetalleViewHolder, position: Int) {
        val telefono = telefonos[position]
        holder.bind(telefono)
        holder.itemView.findViewById<ImageView>(R.id.updateButton).setOnClickListener {
            onUpdateClick(telefono)
        }
        holder.itemView.findViewById<ImageView>(R.id.deleteButton).setOnClickListener {
            onDeleteClick(telefono)
        }
    }

    override fun getItemCount(): Int = telefonos.size

    fun updateList(newList: List<TelefonoResponse>) {
        telefonos = newList
        notifyDataSetChanged()
    }
}
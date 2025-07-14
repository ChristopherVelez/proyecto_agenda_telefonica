package com.example.proyecto_agenda_telefonica

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TelefonoDetalleAdapter(
    private var telefonos: List<TelefonoResponse>
) : RecyclerView.Adapter<TelefonoDetalleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TelefonoDetalleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_telefono_detalle, parent, false)
        return TelefonoDetalleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TelefonoDetalleViewHolder, position: Int) {
        holder.bind(telefonos[position])
    }

    override fun getItemCount(): Int = telefonos.size

    fun updateList(newList: List<TelefonoResponse>) {
        telefonos = newList
        notifyDataSetChanged()
    }
}
package com.example.proyecto_agenda_telefonica

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TelefonoDetalleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvNumero: TextView = view.findViewById(R.id.tvNumero)
    private val tvTipo: TextView = view.findViewById(R.id.tvTipo)

    fun bind(telefono: TelefonoResponse) {
        tvNumero.text = telefono.telefono
        tvTipo.text = "Tipo: ${telefono.tipo}"
    }
}
package com.example.proyecto_agenda_telefonica

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tvCedula: TextView = view.findViewById(R.id.tvCedula)
    private val tvNombre: TextView = view.findViewById(R.id.tvNombre)
    private val tvTelefonos: TextView = view.findViewById(R.id.tvTelefono)

    fun bind(data: UsuarioConTelefonos) {
        tvCedula.text = "Cédula: ${data.usuario.cedula}"
        tvNombre.text = "Nombre: ${data.usuario.nombre} ${data.usuario.apellido}"
        tvTelefonos.text = "Teléfono: ${data.telefonos.firstOrNull()?.telefono ?: "Sin número"}"
    }
}

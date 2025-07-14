package com.example.proyecto_agenda_telefonica

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

class UsuarioAdapter(
    private var usuarios: List<UsuarioConTelefonos>,
    private val onClick: (UsuarioConTelefonos) -> Unit
) : RecyclerView.Adapter<UsuarioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UsuarioViewHolder(layoutInflater.inflate(R.layout.item_telefono, parent, false))
    }

    override fun getItemCount() = usuarios.size

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        holder.bind(usuarios[position])
        holder.itemView.setOnClickListener { onClick(usuarios[position]) }
    }

    fun updateList(newList: List<UsuarioConTelefonos>) {
        usuarios = newList
        notifyDataSetChanged()
    }
}

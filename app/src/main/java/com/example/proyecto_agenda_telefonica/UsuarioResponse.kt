package com.example.proyecto_agenda_telefonica


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsuarioResponse(
    val id: Int? = null,
    val nombre: String,
    val apellido: String,
    val apodo: String,
    val cedula: String
) : Parcelable


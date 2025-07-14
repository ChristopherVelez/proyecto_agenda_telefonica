package com.example.proyecto_agenda_telefonica

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TelefonoResponse(
    val id: Int? = null,
    val usuario_id: Int,
    val telefono: String,
    val tipo: String
) : Parcelable


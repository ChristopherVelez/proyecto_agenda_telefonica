package com.example.proyecto_agenda_telefonica

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsuarioConTelefonos(
    val usuario: UsuarioResponse,
    val telefonos: List<TelefonoResponse>
) : Parcelable

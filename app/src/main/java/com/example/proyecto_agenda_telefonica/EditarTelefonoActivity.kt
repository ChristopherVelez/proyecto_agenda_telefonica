package com.example.proyecto_agenda_telefonica

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import APIService
import android.widget.ArrayAdapter
import com.example.proyecto_agenda_telefonica.databinding.ActivityEditarTelefonoBinding

class EditarTelefonoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarTelefonoBinding
    private var telefonoActual: TelefonoResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditarTelefonoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        telefonoActual = intent.getParcelableExtra("telefono")

        if (telefonoActual == null) {
            Toast.makeText(this, "Error: teléfono no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }


        val tipos = listOf("Casa", "Celular", "Trabajo", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        binding.spinnerTipoEdit.adapter = adapter

        binding.etNumero.setText(telefonoActual?.telefono)
        val posicion = tipos.indexOf(telefonoActual?.tipo)
        if (posicion >= 0) binding.spinnerTipoEdit.setSelection(posicion)

        binding.btnGuardar.setOnClickListener { editarTelefono() }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun editarTelefono() {
        val nuevoNumero = binding.etNumero.text.toString().trim()
        val nuevoTipo = binding.spinnerTipoEdit.selectedItem.toString()
        if (nuevoTipo.isEmpty() || nuevoTipo !in listOf("Casa", "Celular", "Trabajo", "Otro")) {
            Toast.makeText(this, "Tipo de teléfono no válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (nuevoNumero.length != 10 || !nuevoNumero.all { it.isDigit() }) {
            Toast.makeText(this, "Número inválido. Deben ser 10 dígitos.", Toast.LENGTH_SHORT).show()
            return
        }

        val telefonoEditado = TelefonoResponse(
            id = telefonoActual?.id,
            usuario_id = telefonoActual?.usuario_id ?: 0,
            telefono = nuevoNumero,
            tipo = nuevoTipo
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.getInstance().create(APIService::class.java)
                val response = api.updateTelefono(telefonoEditado)

                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditarTelefonoActivity, "Teléfono actualizado", Toast.LENGTH_SHORT).show()
                        finish()  // Volver a la pantalla anterior
                    } else {
                        Toast.makeText(this@EditarTelefonoActivity, "Error al actualizar teléfono", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@EditarTelefonoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
package com.example.proyecto_agenda_telefonica

import APIService
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto_agenda_telefonica.databinding.ActivityAgregarTelefonoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgregarTelefonoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAgregarTelefonoBinding
    private var usuarioId: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgregarTelefonoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioId = intent.getIntExtra("usuario_id", -1)
        if (usuarioId == -1) {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val tipos = listOf("Casa", "Celular", "Trabajo", "Otro")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)
        binding.spinnerTipo.adapter = adapter

        binding.btnGuardarTelefono.setOnClickListener {
            agregarTelefono()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun agregarTelefono() {
        val telefono = binding.etTelefono.text.toString().trim()
        val tipo = binding.spinnerTipo.selectedItem.toString()
        if (tipo.isEmpty() || tipo !in listOf("Casa", "Celular", "Trabajo", "Otro")) {
            Toast.makeText(this, "Tipo de teléfono no válido", Toast.LENGTH_SHORT).show()
            return
        }
        if (telefono.length != 10 || !telefono.all { it.isDigit() }) {
            Toast.makeText(this, "Número inválido. Deben ser 10 dígitos.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val nuevoTelefono = TelefonoResponse(
            usuario_id = usuarioId,
            telefono = telefono,
            tipo = tipo
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.getInstance().create(APIService::class.java)
                val response = api.createTelefono(nuevoTelefono)
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@AgregarTelefonoActivity,
                            "Teléfono agregado",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@AgregarTelefonoActivity,
                            "Error al guardar teléfono",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@AgregarTelefonoActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
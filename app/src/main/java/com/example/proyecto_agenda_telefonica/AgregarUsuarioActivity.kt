package com.example.proyecto_agenda_telefonica

import APIService
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto_agenda_telefonica.databinding.ActivityAgregarUsuarioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AgregarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAgregarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar evento de guardar
        binding.btnGuardar.setOnClickListener {
            agregarUsuario()
        }

        // Ajustes visuales
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun agregarUsuario() {
        val cedula = binding.etCedula.text.toString().trim()
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val apodo = binding.etApodo.text.toString().trim()

        if (cedula.isEmpty() || nombre.isEmpty() || apellido.isEmpty() || apodo.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val nuevoUsuario = UsuarioResponse(
            nombre = nombre,
            apellido = apellido,
            apodo = apodo,
            cedula = cedula
        )

        guardarUsuario(nuevoUsuario)
    }

    private fun guardarUsuario(usuario: UsuarioResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            val api = RetrofitClient.getInstance().create(APIService::class.java)
            val response = api.createUsuario(usuario)

            runOnUiThread {
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@AgregarUsuarioActivity,
                        "Usuario guardado con éxito",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // Regresar a la pantalla anterior
                } else {
                    Toast.makeText(
                        this@AgregarUsuarioActivity,
                        "Error al guardar el usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

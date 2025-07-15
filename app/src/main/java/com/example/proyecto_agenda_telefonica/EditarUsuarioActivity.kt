package com.example.proyecto_agenda_telefonica

import APIService
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto_agenda_telefonica.databinding.ActivityEditarUsuarioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditarUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditarUsuarioBinding
    private var usuario: UsuarioResponse? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        usuario = intent.getParcelableExtra("usuario")


        if (usuario == null) {
            Toast.makeText(this, "Usuario no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.etNombre.setText(usuario!!.nombre)
        binding.etApellido.setText(usuario!!.apellido)
        binding.etApodo.setText(usuario!!.apodo)
        binding.etCedula.setText(usuario!!.cedula)

        binding.btnEditar.setOnClickListener {
            editarUsuario()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun editarUsuario() {
        val nombre = binding.etNombre.text.toString().trim()
        val apellido = binding.etApellido.text.toString().trim()
        val apodo = binding.etApodo.text.toString().trim()
        val cedula = binding.etCedula.text.toString().trim()

        if (nombre.isEmpty() || apellido.isEmpty() || apodo.isEmpty() || cedula.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (!cedula.matches(Regex("\\d{10}"))) {
            Toast.makeText(this, "La cédula debe tener exactamente 10 dígitos", Toast.LENGTH_SHORT).show()
            return
        }

        val usuarioEditado = UsuarioResponse(
            id = usuario?.id,
            nombre = nombre,
            apellido = apellido,
            apodo = apodo,
            cedula = cedula
        )

        CoroutineScope(Dispatchers.IO).launch {
            val api = RetrofitClient.getInstance().create(APIService::class.java)
            val response = api.updateUsuario(usuarioEditado)

            runOnUiThread {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditarUsuarioActivity, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditarUsuarioActivity, "Error al actualizar usuario", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
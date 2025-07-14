package com.example.proyecto_agenda_telefonica

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto_agenda_telefonica.databinding.ActivityDetalleUsuarioBinding

class DetalleUsuarioActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetalleUsuarioBinding
    private lateinit var adapter: TelefonoDetalleAdapter
    private lateinit var usuarioConTelefonos: UsuarioConTelefonos
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetalleUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usuarioConTelefonos = intent.getParcelableExtra("usuario")!!
        mostrarDatosUsuario()
        initRecyclerView()

        binding.btnEditarUsuario.setOnClickListener {
            Toast.makeText(this, "Editar usuario (pendiente)", Toast.LENGTH_SHORT).show()
        }

        binding.btnEliminarUsuario.setOnClickListener {
            Toast.makeText(this, "Eliminar usuario (pendiente)", Toast.LENGTH_SHORT).show()
        }

        binding.btnAgregarTelefono.setOnClickListener {
            Toast.makeText(this, "Agregar teléfono (pendiente)", Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun mostrarDatosUsuario() {
        with(binding) {
            tvCedula.text = "Cédula: ${usuarioConTelefonos.usuario.cedula}"
            tvNombre.text = "Nombre: ${usuarioConTelefonos.usuario.nombre}"
            tvApellido.text = "Apellido: ${usuarioConTelefonos.usuario.apellido}"
            tvApodo.text = "Apodo: ${usuarioConTelefonos.usuario.apodo}"
        }
    }

    private fun initRecyclerView() {
        adapter = TelefonoDetalleAdapter(usuarioConTelefonos.telefonos)
        binding.recyclerTelefonos.layoutManager = LinearLayoutManager(this)
        binding.recyclerTelefonos.adapter = adapter
    }

}
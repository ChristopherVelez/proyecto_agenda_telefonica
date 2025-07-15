package com.example.proyecto_agenda_telefonica

import APIService
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.proyecto_agenda_telefonica.databinding.ActivityDetalleUsuarioBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            val intent = Intent(this, EditarUsuarioActivity::class.java)
            intent.putExtra("usuario", usuarioConTelefonos.usuario)
            startActivity(intent)
        }

        binding.btnEliminarUsuario.setOnClickListener {
            eliminarUsuario()
        }



        binding.btnAgregarTelefono.setOnClickListener {
            val intent = Intent(this, AgregarTelefonoActivity::class.java)
            intent.putExtra("usuario_id", usuarioConTelefonos.usuario.id)
            startActivity(intent)
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
        adapter = TelefonoDetalleAdapter(
            usuarioConTelefonos.telefonos,
            onUpdateClick = { telefono ->
                val intent = Intent(this, EditarTelefonoActivity::class.java)
                intent.putExtra("telefono", telefono)
                startActivity(intent)
            },
            onDeleteClick = { telefono ->
                eliminarTelefono(telefono)
            }
        )
        binding.recyclerTelefonos.layoutManager = LinearLayoutManager(this)
        binding.recyclerTelefonos.adapter = adapter
    }

    private fun eliminarUsuario() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.getInstance().create(APIService::class.java)
                val response = api.deleteUsuario(mapOf("id" to (usuarioConTelefonos.usuario.id ?: 0)))

                runOnUiThread {
                    if (response.isSuccessful) {
                        val mensaje = response.body()?.get("message") ?: "Usuario eliminado correctamente"
                        Toast.makeText(this@DetalleUsuarioActivity, mensaje, Toast.LENGTH_SHORT).show()
                        finish() // Salir de esta pantalla
                    } else {
                        Toast.makeText(this@DetalleUsuarioActivity, "Error al eliminar usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@DetalleUsuarioActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun eliminarTelefono(telefono: TelefonoResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.getInstance().create(APIService::class.java)
                val response = api.deleteTelefono(mapOf("id" to (telefono.id ?: 0)))

                runOnUiThread {
                    if (response.isSuccessful) {
                        val mensaje = response.body()?.get("message") ?: "Teléfono eliminado"
                        Toast.makeText(this@DetalleUsuarioActivity, mensaje, Toast.LENGTH_SHORT).show()

                        recargarTelefonos()
                    } else {
                        Toast.makeText(this@DetalleUsuarioActivity, "Error al eliminar teléfono", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@DetalleUsuarioActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun recargarTelefonos() {
        val usuarioId = usuarioConTelefonos.usuario.id

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.getInstance().create(APIService::class.java)
                val response = api.getAllTelefonos()

                if (response.isSuccessful) {
                    val telefonos = response.body()?.filter { it.usuario_id == usuarioId } ?: emptyList()
                    runOnUiThread {
                        adapter.updateList(telefonos)
                        usuarioConTelefonos = usuarioConTelefonos.copy(telefonos = telefonos) // también actualizamos el objeto local
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@DetalleUsuarioActivity,
                            "No se pudieron cargar los teléfonos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@DetalleUsuarioActivity,
                        "Error al cargar teléfonos: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun recargarUsuario() {
        val usuarioId = usuarioConTelefonos.usuario.id ?: return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.getInstance().create(APIService::class.java)
                val response = api.getUsuarioById(usuarioId)

                if (response.isSuccessful) {
                    val usuarioActualizado = response.body()
                    runOnUiThread {
                        if (usuarioActualizado != null) {
                            usuarioConTelefonos = usuarioConTelefonos.copy(usuario = usuarioActualizado)
                            mostrarDatosUsuario()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@DetalleUsuarioActivity, "No se pudo cargar el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@DetalleUsuarioActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        recargarUsuario()

        recargarTelefonos()

    }

}
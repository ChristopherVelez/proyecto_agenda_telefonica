package com.example.proyecto_agenda_telefonica

import APIService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto_agenda_telefonica.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UsuarioAdapter
    private var listaOriginal = listOf<UsuarioConTelefonos>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        cargarUsuariosConTelefonos()
        binding.btnLogout.setOnClickListener {
            cerrarSesion()
        }


        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AgregarUsuarioActivity::class.java))
        }

        binding.searchView.setOnQueryTextListener(this)



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
    }

    private fun cerrarSesion() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.getInstance().create(APIService::class.java)
                val response = api.logout()

                runOnUiThread {
                    if (response.isSuccessful) {
                        eliminarToken()
                        irALogin()
                    } else {
                        Toast.makeText(this@MainActivity, "Error al cerrar sesión", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun eliminarToken() {
        val prefs = getSharedPreferences("agenda_prefs", MODE_PRIVATE)
        prefs.edit().remove("jwt_token").apply()
    }

    private fun irALogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    private fun initRecyclerView() {
        adapter = UsuarioAdapter(emptyList()) { usuarioSeleccionado ->
            val intent = Intent(this, DetalleUsuarioActivity::class.java)
            intent.putExtra("usuario", usuarioSeleccionado)
            startActivity(intent)

        }
        binding.recyclerTelefonos.layoutManager = LinearLayoutManager(this)
        binding.recyclerTelefonos.adapter = adapter
    }
    private fun cargarUsuariosConTelefonos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.getInstance().create(APIService::class.java)

                val usuarios = obtenerUsuarios(api)
                val telefonos = obtenerTelefonos(api)
                val datosAgrupados = agruparDatos(usuarios, telefonos)

                runOnUiThread {
                    listaOriginal = datosAgrupados
                    adapter.updateList(datosAgrupados)
                }
            } catch (ex: Exception) {
                runOnUiThread { mostrarError() }
            }
        }
    }



    private fun filtrarUsuarios(texto: String?) {
        val query = texto?.trim()?.lowercase() ?: ""
        val filtrados = if (query.isEmpty()) {
            listaOriginal
        } else {
            listaOriginal.filter { usuario ->
                usuario.usuario.nombre.lowercase().contains(query) ||
                        usuario.usuario.apellido.lowercase().contains(query) ||
                        usuario.usuario.cedula.lowercase().contains(query) ||
                        usuario.usuario.apodo.lowercase().contains(query) ||
                        usuario.telefonos.any {
                            it.telefono.contains(query) || it.tipo.lowercase().contains(query)
                        }
            }
        }
        adapter.updateList(filtrados)
    }

    private suspend fun obtenerUsuarios(api: APIService): List<UsuarioResponse> {
        val response = api.getAllUsuarios()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        throw Exception("Error al obtener usuarios")
    }

    private suspend fun obtenerTelefonos(api: APIService): List<TelefonoResponse> {
        val response = api.getAllTelefonos()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        throw Exception("Error al obtener teléfonos")
    }

    private fun agruparDatos(
        usuarios: List<UsuarioResponse>,
        telefonos: List<TelefonoResponse>
    ): List<UsuarioConTelefonos> {
        return usuarios.map { usuario ->
            UsuarioConTelefonos(
                usuario = usuario,
                telefonos = telefonos.filter { it.usuario_id == usuario.id }
            )
        }
    }


    private fun mostrarError() {
        Toast.makeText(this,"Ocurrio un error", Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
        cargarUsuariosConTelefonos()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        filtrarUsuarios(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        filtrarUsuarios(newText)
        return true
    }
}
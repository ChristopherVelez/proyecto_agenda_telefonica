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
                val usuariosResponse = api.getAllUsuarios()
                val telefonosResponse = api.getAllTelefonos()

                runOnUiThread {
                    if (usuariosResponse.isSuccessful && telefonosResponse.isSuccessful) {
                        val usuarios = usuariosResponse.body() ?: emptyList()
                        val telefonos = telefonosResponse.body() ?: emptyList()

                        val agrupados = usuarios.map { usuario ->
                            UsuarioConTelefonos(
                                usuario = usuario,
                                telefonos = telefonos.filter { it.usuario_id == usuario.id }
                            )
                        }

                        listaOriginal = agrupados
                        adapter.updateList(agrupados)
                    } else {
                        mostrarError()
                    }
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

    private fun mostrarError() {
        Toast.makeText(this,"Ocurrio un error", Toast.LENGTH_SHORT).show()
    }
    override fun onResume() {
        super.onResume()
        // Refrescar al volver a la actividad
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
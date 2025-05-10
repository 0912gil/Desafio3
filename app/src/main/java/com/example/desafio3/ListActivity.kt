package com.example.desafio3

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecursoAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var etBuscarId: EditText
    private lateinit var btnBuscar: Button
    private lateinit var tvNoResultados: TextView
    private var recursos = mutableListOf<Recurso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewRecursos)
        progressBar = findViewById(R.id.progressBar)
        etBuscarId = findViewById(R.id.etBuscarId)
        btnBuscar = findViewById(R.id.btnBuscar)
        tvNoResultados = findViewById(R.id.tvNoResultados)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecursoAdapter(recursos) { recurso ->
            // Click en un recurso para editarlo
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("RECURSO_ID", recurso.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Botón para buscar por ID
        btnBuscar.setOnClickListener {
            val id = etBuscarId.text.toString().trim()
            if (id.isNotEmpty()) {
                buscarRecursoPorId(id)
            } else {
                // Si el campo está vacío, mostrar todos los recursos
                cargarRecursos()
            }
        }

        // Cargar todos los recursos al iniciar
        cargarRecursos()
    }

    private fun cargarRecursos() {
        showLoading(true)
        tvNoResultados.visibility = View.GONE

        RetrofitClient.instance.obtenerRecursos().enqueue(object : Callback<List<Recurso>> {
            override fun onResponse(call: Call<List<Recurso>>, response: Response<List<Recurso>>) {
                showLoading(false)
                if (response.isSuccessful) {
                    response.body()?.let {
                        recursos.clear()
                        recursos.addAll(it)
                        adapter.notifyDataSetChanged()

                        if (recursos.isEmpty()) {
                            tvNoResultados.visibility = View.VISIBLE
                        }
                    }
                } else {
                    Toast.makeText(
                        this@ListActivity,
                        "Error al cargar los recursos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Recurso>>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@ListActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun buscarRecursoPorId(id: String) {
        showLoading(true)
        tvNoResultados.visibility = View.GONE

        RetrofitClient.instance.obtenerRecursoPorId(id).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                showLoading(false)
                if (response.isSuccessful && response.body() != null) {
                    recursos.clear()
                    recursos.add(response.body()!!)
                    adapter.notifyDataSetChanged()
                } else {
                    recursos.clear()
                    adapter.notifyDataSetChanged()
                    tvNoResultados.visibility = View.VISIBLE
                    Toast.makeText(
                        this@ListActivity,
                        "Recurso no encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@ListActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Método para actualizar la lista después de regresar de otras actividades
    override fun onResume() {
        super.onResume()
        cargarRecursos()
    }
}
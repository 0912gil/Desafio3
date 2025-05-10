package com.example.desafio3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteActivity : AppCompatActivity() {
    private lateinit var etBuscarId: EditText
    private lateinit var btnBuscar: Button
    private lateinit var btnEliminar: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var tvTitulo: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvTipo: TextView
    private lateinit var tvEnlace: TextView
    private lateinit var layoutDetalles: View

    private var recursoId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        // Inicializar vistas
        etBuscarId = findViewById(R.id.etBuscarId)
        btnBuscar = findViewById(R.id.btnBuscar)
        btnEliminar = findViewById(R.id.btnEliminar)
        progressBar = findViewById(R.id.progressBar)
        tvTitulo = findViewById(R.id.tvTitulo)
        tvDescripcion = findViewById(R.id.tvDescripcion)
        tvTipo = findViewById(R.id.tvTipo)
        tvEnlace = findViewById(R.id.tvEnlace)
        layoutDetalles = findViewById(R.id.layoutDetalles)

        // Ocultar detalles inicialmente
        layoutDetalles.visibility = View.GONE
        btnEliminar.visibility = View.GONE

        // Configurar botón de búsqueda
        btnBuscar.setOnClickListener {
            val id = etBuscarId.text.toString().trim()
            if (id.isNotEmpty()) {
                buscarRecursoPorId(id)
            } else {
                Toast.makeText(this, "Ingrese un ID válido", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar botón de eliminación
        btnEliminar.setOnClickListener {
            mostrarDialogoConfirmacion()
        }
    }

    private fun buscarRecursoPorId(id: String) {
        showLoading(true)
        layoutDetalles.visibility = View.GONE
        btnEliminar.visibility = View.GONE

        RetrofitClient.instance.obtenerRecursoPorId(id).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    // Guardar ID para eliminar
                    recursoId = response.body()!!.id

                    // Mostrar detalles
                    tvTitulo.text = response.body()!!.titulo
                    tvDescripcion.text = response.body()!!.descripcion
                    tvTipo.text = "Tipo: ${response.body()!!.tipo}"
                    tvEnlace.text = "Enlace: ${response.body()!!.enlace}"

                    layoutDetalles.visibility = View.VISIBLE
                    btnEliminar.visibility = View.VISIBLE
                } else {
                    Toast.makeText(
                        this@DeleteActivity,
                        "Recurso no encontrado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@DeleteActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun eliminarRecurso() {
        showLoading(true)

        RetrofitClient.instance.eliminarRecurso(recursoId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                showLoading(false)

                if (response.isSuccessful) {
                    Toast.makeText(
                        this@DeleteActivity,
                        "Recurso eliminado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Limpiar campos y ocultar detalles
                    etBuscarId.text.clear()
                    layoutDetalles.visibility = View.GONE
                    btnEliminar.visibility = View.GONE
                } else {
                    Toast.makeText(
                        this@DeleteActivity,
                        "Error al eliminar el recurso",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@DeleteActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun mostrarDialogoConfirmacion() {
        AlertDialog.Builder(this)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar este recurso? Esta acción no se puede deshacer.")
            .setPositiveButton("Eliminar") { _, _ ->
                eliminarRecurso()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnBuscar.isEnabled = !isLoading
        btnEliminar.isEnabled = !isLoading
    }
}
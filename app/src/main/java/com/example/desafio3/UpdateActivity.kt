package com.example.desafio3

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : AppCompatActivity() {
    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etTipo: EditText
    private lateinit var etEnlace: EditText
    private lateinit var etImagen: EditText
    private lateinit var btnActualizar: Button
    private lateinit var progressBar: ProgressBar

    private var recursoId: String = ""
    private lateinit var recursoActual: Recurso

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        // Inicializar vistas
        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        etTipo = findViewById(R.id.etTipo)
        etEnlace = findViewById(R.id.etEnlace)
        etImagen = findViewById(R.id.etImagen)
        btnActualizar = findViewById(R.id.btnActualizar)
        progressBar = findViewById(R.id.progressBar)

        // Obtener el ID del recurso pasado desde la actividad anterior
        recursoId = intent.getStringExtra("RECURSO_ID") ?: ""

        if (recursoId.isEmpty()) {
            Toast.makeText(this, "ID de recurso no válido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Cargar los datos del recurso
        cargarDatosRecurso()

        // Configurar botón de actualización
        btnActualizar.setOnClickListener {
            actualizarRecurso()
        }
    }

    private fun cargarDatosRecurso() {
        showLoading(true)

        RetrofitClient.instance.obtenerRecursoPorId(recursoId).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                showLoading(false)

                if (response.isSuccessful && response.body() != null) {
                    recursoActual = response.body()!!

                    // Mostrar los datos en los campos
                    etTitulo.setText(recursoActual.titulo)
                    etDescripcion.setText(recursoActual.descripcion)
                    etTipo.setText(recursoActual.tipo)
                    etEnlace.setText(recursoActual.enlace)
                    etImagen.setText(recursoActual.imagen)
                } else {
                    Toast.makeText(
                        this@UpdateActivity,
                        "No se pudo cargar el recurso",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@UpdateActivity,
                    "Error de conexión: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        })
    }

    private fun actualizarRecurso() {
        val titulo = etTitulo.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val tipo = etTipo.text.toString().trim()
        val enlace = etEnlace.text.toString().trim()
        val imagen = etImagen.text.toString().trim()

        // Validar campos
        if (titulo.isEmpty() || descripcion.isEmpty() || tipo.isEmpty() || enlace.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear objeto actualizado
        val recursoActualizado = Recurso(
            id = recursoId,
            titulo = titulo,
            descripcion = descripcion,
            tipo = tipo,
            enlace = enlace,
            imagen = if (imagen.isEmpty()) "https://via.placeholder.com/150" else imagen
        )

        showLoading(true)

        RetrofitClient.instance.actualizarRecurso(recursoId, recursoActualizado)
            .enqueue(object : Callback<Recurso> {
                override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                    showLoading(false)

                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UpdateActivity,
                            "Recurso actualizado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@UpdateActivity,
                            "Error al actualizar el recurso",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Recurso>, t: Throwable) {
                    showLoading(false)
                    Toast.makeText(
                        this@UpdateActivity,
                        "Error de conexión: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        btnActualizar.isEnabled = !isLoading
    }
}
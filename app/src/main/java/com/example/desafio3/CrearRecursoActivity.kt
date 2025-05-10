package com.example.desafio3

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearRecursoActivity : AppCompatActivity() {
    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etTipo: EditText
    private lateinit var etEnlace: EditText
    private lateinit var etImagen: EditText
    private lateinit var btnGuardar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_recurso)

        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        etTipo = findViewById(R.id.etTipo)
        etEnlace = findViewById(R.id.etEnlace)
        etImagen = findViewById(R.id.etImagen)
        btnGuardar = findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            guardarRecurso()
        }
    }

    private fun guardarRecurso() {
        val titulo = etTitulo.text.toString().trim()
        val descripcion = etDescripcion.text.toString().trim()
        val tipo = etTipo.text.toString().trim()
        val enlace = etEnlace.text.toString().trim()
        val imagen = etImagen.text.toString().trim()

        if (titulo.isEmpty() || descripcion.isEmpty() || tipo.isEmpty() || enlace.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val recurso = Recurso(
            titulo = titulo,
            descripcion = descripcion,
            tipo = tipo,
            enlace = enlace,
            imagen = if (imagen.isEmpty()) "https://via.placeholder.com/150" else imagen
        )

        RetrofitClient.instance.crearRecurso(recurso).enqueue(object : Callback<Recurso> {
            override fun onResponse(call: Call<Recurso>, response: Response<Recurso>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CrearRecursoActivity, "Recurso creado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CrearRecursoActivity, "Error al crear recurso", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Recurso>, t: Throwable) {
                Toast.makeText(this@CrearRecursoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
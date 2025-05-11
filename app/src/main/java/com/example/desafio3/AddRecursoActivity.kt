package com.example.desafio3

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class AddRecursoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recurso)

        val titulo = findViewById<EditText>(R.id.etTitulo)
        val descripcion = findViewById<EditText>(R.id.etDescripcion)
        val tipo = findViewById<EditText>(R.id.etTipo)
        val enlace = findViewById<EditText>(R.id.etEnlace)
        val imagen = findViewById<EditText>(R.id.etImagen)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            val tituloText = titulo.text.toString().trim()
            val descripcionText = descripcion.text.toString().trim()
            val tipoText = tipo.text.toString().trim()
            val enlaceText = enlace.text.toString().trim()
            val imagenText = imagen.text.toString().trim()

            // Validación
            if (tituloText.isEmpty() || descripcionText.isEmpty() || tipoText.isEmpty() || enlaceText.isEmpty() || imagenText.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("recursos")
            val recursoId = ref.push().key ?: return@setOnClickListener

            val recurso = Recurso(
                id = recursoId.hashCode(),
                titulo = tituloText,
                descripcion = descripcionText,
                tipo = tipoText,
                enlace = enlaceText,
                imagen = imagenText
            )

            ref.child(recursoId).setValue(recurso).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Recurso guardado correctamente", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
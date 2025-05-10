package com.example.desafio3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        auth = FirebaseAuth.getInstance()

        val btnCreate = findViewById<Button>(R.id.btnCreate)
        val btnRead = findViewById<Button>(R.id.btnRead)
        val btnDelete = findViewById<Button>(R.id.btnDelete)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        btnCreate.setOnClickListener {
            startActivity(Intent(this, CrearRecursoActivity::class.java))
        }

        btnRead.setOnClickListener {
            startActivity(Intent(this, ListActivity::class.java))
        }

        btnDelete.setOnClickListener {
            startActivity(Intent(this, DeleteActivity::class.java))
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}

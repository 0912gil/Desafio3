package com.example.desafio3

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecursoAdapter
    private val recursoList = mutableListOf<Recurso>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.recyclerRecursos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecursoAdapter(recursoList)
        recyclerView.adapter = adapter

        val ref = FirebaseDatabase.getInstance().getReference("recursos")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recursoList.clear()
                for (recursoSnapshot in snapshot.children) {
                    val recurso = recursoSnapshot.getValue(Recurso::class.java)
                    recurso?.let { recursoList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

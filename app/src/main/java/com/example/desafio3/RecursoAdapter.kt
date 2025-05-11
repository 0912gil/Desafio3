package com.example.desafio3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView


class RecursoAdapter(private val items: List<Recurso>) : RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder>() {

    // ViewHolder que contendrá las vistas para cada ítem
    class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Aquí puedes declarar las vistas que quieres acceder, como un TextView
        val textView: TextView = itemView.findViewById(R.id.tvTitulo)
    }

    // Inflar el layout para cada ítem
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recurso, parent, false)
        return RecursoViewHolder(view)
    }

    // Bindear los datos con las vistas
    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = items[position]
        holder.textView.text = recurso.titulo
    }

    // Retorna el tamaño de la lista
    override fun getItemCount(): Int {
        return items.size
    }
}

package com.example.desafio3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecursoAdapter(
    private val recursos: List<Recurso>,
    private val onItemClick: (Recurso) -> Unit
) : RecyclerView.Adapter<RecursoAdapter.RecursoViewHolder>() {

    class RecursoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        val ivImagen: ImageView = itemView.findViewById(R.id.ivImagen)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recurso, parent, false)
        return RecursoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        val recurso = recursos[position]

        holder.tvTitulo.text = recurso.titulo
        holder.tvDescripcion.text = recurso.descripcion
        holder.tvTipo.text = "Tipo: ${recurso.tipo}"

        // Cargar imagen con Glide
        Glide.with(holder.itemView.context)
            .load(recurso.imagen)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.ivImagen)

        // Configurar el evento de clic
        holder.itemView.setOnClickListener {
            onItemClick(recurso)
        }
    }

    override fun getItemCount(): Int = recursos.size
}
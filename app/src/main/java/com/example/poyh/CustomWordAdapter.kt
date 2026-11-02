package com.example.poyh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomWordAdapter(
    private val words: MutableList<String>,
    private val onWordRemoved: (Int) -> Unit
) : RecyclerView.Adapter<CustomWordAdapter.WordViewHolder>() {

    class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCustomWord: TextView = itemView.findViewById(R.id.tvCustomWord)
        val btnRemoveWord: ImageButton = itemView.findViewById(R.id.btnRemoveWord)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_custom_word, parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.tvCustomWord.text = words[position]
        holder.btnRemoveWord.setOnClickListener {
            val currentPos = holder.adapterPosition
            if (currentPos != RecyclerView.NO_POSITION) {
                onWordRemoved(currentPos)
            }
        }
    }

    override fun getItemCount(): Int = words.size
}

package com.example.poyh

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class WordResultAdapter(
    private val results: List<WordResult>
) : RecyclerView.Adapter<WordResultAdapter.ResultViewHolder>() {

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWord: TextView = itemView.findViewById(R.id.tvResultWord)
        val tvStatusIcon: TextView = itemView.findViewById(R.id.tvStatusIcon)

        fun bind(result: WordResult) {
            tvWord.text = result.word

            if (result.isGuessed) {
                tvStatusIcon.text = "✔"
                tvStatusIcon.setTextColor(ContextCompat.getColor(itemView.context, R.color.feedback_correct))
            } else {
                tvStatusIcon.text = "✖"
                tvStatusIcon.setTextColor(ContextCompat.getColor(itemView.context, R.color.feedback_pass))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_word_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size
}

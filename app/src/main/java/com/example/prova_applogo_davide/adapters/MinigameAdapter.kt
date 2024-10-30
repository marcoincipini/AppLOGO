package com.example.prova_applogo_davide.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prova_applogo_davide.R
import com.example.prova_applogo_davide.models.Minigame

class MinigameAdapter(
    private val minigames: List<Minigame>
) : RecyclerView.Adapter<MinigameAdapter.MinigameViewHolder>() {

    inner class MinigameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        val startButton: Button = itemView.findViewById(R.id.buttonStart)

        fun bind(minigame: Minigame) {
            titleTextView.text = minigame.title
            descriptionTextView.text = minigame.description
            startButton.setOnClickListener {
                // Simula l’avvio del minigioco (eventualmente salva un risultato placeholder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinigameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_minigame, parent, false)
        return MinigameViewHolder(view)
    }

    override fun onBindViewHolder(holder: MinigameViewHolder, position: Int) {
        holder.bind(minigames[position])
    }

    override fun getItemCount(): Int {
        return minigames.size
    }
}

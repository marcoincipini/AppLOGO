package com.example.prova_applogo_davide.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prova_applogo_davide.R
import com.example.prova_applogo_davide.adapters.MinigameAdapter
import com.example.prova_applogo_davide.models.Minigame

class GamesActivity : AppCompatActivity() {
    private lateinit var recyclerViewMinigames: RecyclerView
    private lateinit var minigames: List<Minigame> // Placeholder per i minigiochi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        recyclerViewMinigames = findViewById(R.id.recyclerViewMinigames)
        minigames = getPlaceholderMinigames() // Inizializza i minigiochi come placeholder

        recyclerViewMinigames.layoutManager = LinearLayoutManager(this)
        recyclerViewMinigames.adapter = MinigameAdapter(minigames)
    }

    // Funzione per ottenere una lista di minigiochi placeholder
    private fun getPlaceholderMinigames(): List<Minigame> {
        return listOf(
            Minigame("1", "Minigioco Lettoscrittura", "Un minigioco sulla lettoscrittura"),
            Minigame("2", "Minigioco di Matematica", "Un minigioco sulla matematica"),
            Minigame("3", "Minigioco di Memoria", "Un minigioco sulla memoria")
        )
    }
}

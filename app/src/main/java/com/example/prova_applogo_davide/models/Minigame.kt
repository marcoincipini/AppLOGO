package com.example.prova_applogo_davide.models

data class Minigame(
    val id: String,                // Identificativo univoco del minigioco
    val title: String,             // Titolo del minigioco
    val description: String,       // Descrizione del minigioco
    val metadata: String = "",     // Dati informativi sul minigioco (es. versione, tipo)
    val jsonDataPlaceholder: String = "{}"  // Placeholder per i dati JSON del risultato
)
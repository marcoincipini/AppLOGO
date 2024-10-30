package com.example.prova_applogo_davide.models

data class Patient(
    val idAnonimo: String,  // ID principale e unico
    val name: String,
    val surname: String,
    val age: Int,
    val gender: String,
    val privacyAccepted: Boolean
)

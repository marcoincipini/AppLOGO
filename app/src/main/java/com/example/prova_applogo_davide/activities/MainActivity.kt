package com.example.prova_applogo_davide.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prova_applogo_davide.R
import com.example.prova_applogo_davide.adapters.PatientAdapter
import com.example.prova_applogo_davide.database.DatabaseHelper
import com.example.prova_applogo_davide.models.Patient
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), PatientAdapter.OnPatientActionListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddPatient: Button
    private lateinit var btnLogout: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewPatients)
        btnAddPatient = findViewById(R.id.btnAddPatient)
        btnLogout = findViewById(R.id.btnLogout)

        dbHelper = DatabaseHelper(this)

        // Mostra la lista dei pazienti
        loadPatients()

        // Aggiungi paziente
        btnAddPatient.setOnClickListener {
            val intent = Intent(this, AddPatientActivity::class.java)
            startActivity(intent)
        }

        // Logout
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadPatients() {
        val patients = dbHelper.getAllPatients() // Funzione per ottenere i pazienti dal database
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PatientAdapter(patients, this) // Passa "this" come listener
    }

    override fun onResume() {
        super.onResume()
        loadPatients() // Ricarica i pazienti ogni volta che si torna a questa activity
    }

    // Gestisce l'eliminazione del paziente
    override fun onDelete(patient: Patient) {
        dbHelper.deletePatient(patient.idAnonimo) // Usa "idAnonimo" come identificatore
        loadPatients() // Ricarica la lista aggiornata
    }

    // Gestisce il click su un paziente per aprire la schermata dei giochi
    override fun onPatientClick(patient: Patient) {
        val intent = Intent(this, GamesActivity::class.java)
        intent.putExtra("patientId", patient.idAnonimo)  // Passa l'ID anonimo come identificativo
        startActivity(intent)
    }
}


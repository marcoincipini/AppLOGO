package com.example.prova_applogo_davide.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.prova_applogo_davide.R
import com.example.prova_applogo_davide.models.Patient

class PatientAdapter(
    private val patients: List<Patient>,
    private val listener: OnPatientActionListener
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    interface OnPatientActionListener {
        fun onDelete(patient: Patient)  // Funzione di callback per eliminare il paziente
        fun onPatientClick(patient: Patient)  // Funzione di callback per aprire la schermata dei giochi
    }

    inner class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val surnameTextView: TextView = itemView.findViewById(R.id.textViewSurname)
        val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)
        private val nameSurnameContainer: View = itemView.findViewById(R.id.nameSurnameContainer)

        init {
            // Imposta il listener su tutto il contenitore nome-cognome
            nameSurnameContainer.setOnClickListener {
                listener.onPatientClick(patients[bindingAdapterPosition])  // Selezione del paziente
            }
            deleteButton.setOnClickListener {
                listener.onDelete(patients[bindingAdapterPosition])  // Eliminazione del paziente
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]
        holder.nameTextView.text = patient.name
        holder.surnameTextView.text = patient.surname
    }

    override fun getItemCount(): Int {
        return patients.size
    }
}


package com.example.prova_applogo_davide.activities

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prova_applogo_davide.R
import com.example.prova_applogo_davide.database.DatabaseHelper

class AddPatientActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextSurname: EditText
    private lateinit var editTextAge: EditText
    private lateinit var editTextGender: EditText
    private lateinit var checkBoxPrivacy: CheckBox
    private lateinit var btnSave: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)

        editTextName = findViewById(R.id.editTextName)
        editTextSurname = findViewById(R.id.editTextSurname)
        editTextAge = findViewById(R.id.editTextAge)
        editTextGender = findViewById(R.id.editTextGender)
        checkBoxPrivacy = findViewById(R.id.checkBoxPrivacy)
        btnSave = findViewById(R.id.btnSave)

        dbHelper = DatabaseHelper(this)

        btnSave.setOnClickListener {
            val name = editTextName.text.toString()
            val surname = editTextSurname.text.toString()
            val age = editTextAge.text.toString().toIntOrNull()
            val gender = editTextGender.text.toString()
            val privacyAccepted = checkBoxPrivacy.isChecked

            // Ottieni l'ID del logopedista da SharedPreferences
            val sharedPref = getSharedPreferences("AppLogoPrefs", Context.MODE_PRIVATE)
            val logopedistaId = sharedPref.getString("logopedistaId", null)

            if (name.isNotEmpty() && surname.isNotEmpty() && age != null && gender.isNotEmpty() && privacyAccepted && logopedistaId != null) {
                dbHelper.addPatient(name, surname, age, gender, privacyAccepted, logopedistaId)
                Toast.makeText(this, "Paziente aggiunto con successo", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Compila tutti i campi e accetta la privacy", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

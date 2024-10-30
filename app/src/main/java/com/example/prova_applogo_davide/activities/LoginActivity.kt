package com.example.prova_applogo_davide.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prova_applogo_davide.R
import com.google.firebase.auth.FirebaseAuth

/*
- Login: Al clic del pulsante "Login", viene chiamata loginUser, che autentica l'utente con Firebase.
- Salvataggio dell’ID del Logopedista: Dopo il login, l'ID del logopedista (uid dell’utente Firebase) viene salvato in SharedPreferences.
- Navigazione a MainActivity: Dopo il login, l'app passa a MainActivity.
*/
class LoginActivity : AppCompatActivity() {
    // Riferimenti per Firebase Auth
    private lateinit var auth: FirebaseAuth
    // Elementi UI
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inizializza Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Associa i componenti UI con gli ID del layout
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar)

        // Imposta il listener per il pulsante di login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE   // Mostra il caricamento
                loginUser(email, password)              // Avvia il processo di login
            } else {
                Toast.makeText(this, "Inserisci email e password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Funzione per autenticare l'utente con Firebase
    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE  // Nasconde il caricamento
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val logopedistaId = it.uid  // Ottiene l'ID unico del logopedista
                        saveLogopedistaIdToSharedPreferences(logopedistaId) // Salva l'ID
                        navigateToMainActivity()    // Avvia la schermata principale
                    }
                } else {
                    Toast.makeText(this, "Login fallito: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE  // Nasconde il caricamento in caso di errore
                Toast.makeText(this, "Errore di connessione: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    // Salva l'ID del logopedista in SharedPreferences
    private fun saveLogopedistaIdToSharedPreferences(logopedistaId: String) {
        val sharedPref: SharedPreferences = getSharedPreferences("AppLogoPrefs", Context.MODE_PRIVATE)
        val storedId = sharedPref.getString("logopedistaId", null)

        // Salva solo se l'ID non è già presente nelle SharedPreferences
        if (storedId == null) {
            sharedPref.edit().putString("logopedistaId", logopedistaId).apply()
        }
    }

    // Naviga alla MainActivity dopo il login
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

package com.example.prova_applogo_davide.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.prova_applogo_davide.models.Patient
import java.util.UUID

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "AppLogoDB"
        private const val DATABASE_VERSION = 1

        // Tabelle
        const val TABLE_PATIENTS = "pazienti"
        const val TABLE_RESULTS = "risultati"
        const val TABLE_RECORDINGS = "registrazioni"

        // Campi per la tabella dei pazienti
        const val COLUMN_ANONYMOUS_ID = "id_anonimo"  // Nuovo ID principale anonimo
        const val COLUMN_NAME = "nome"
        const val COLUMN_SURNAME = "cognome"
        const val COLUMN_AGE = "eta"
        const val COLUMN_GENDER = "sesso"
        const val COLUMN_PRIVACY_ACCEPTED = "privacy_accettata"
        const val COLUMN_LOGOPEDISTA_ID = "logopedista_id"

        // Campi per la tabella dei risultati
        const val COLUMN_RESULT_ID = "id_risultato"
        const val COLUMN_GAME_ID = "id_minigioco"
        const val COLUMN_JSON_DATA = "json_data"
        const val COLUMN_METADATA = "metadata"

        // Campi per la tabella delle registrazioni
        const val COLUMN_RECORDING_ID = "recording_id"
        const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createPatientsTable = """
            CREATE TABLE $TABLE_PATIENTS (
                $COLUMN_ANONYMOUS_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_SURNAME TEXT,
                $COLUMN_AGE INTEGER,
                $COLUMN_GENDER TEXT,
                $COLUMN_PRIVACY_ACCEPTED INTEGER,
                $COLUMN_LOGOPEDISTA_ID TEXT
            )
        """
        db.execSQL(createPatientsTable)

        val createResultsTable = """
            CREATE TABLE $TABLE_RESULTS (
                $COLUMN_RESULT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ANONYMOUS_ID TEXT,
                $COLUMN_GAME_ID TEXT,
                $COLUMN_JSON_DATA TEXT,
                $COLUMN_METADATA TEXT,
                FOREIGN KEY ($COLUMN_ANONYMOUS_ID) REFERENCES $TABLE_PATIENTS($COLUMN_ANONYMOUS_ID)
            )
        """
        db.execSQL(createResultsTable)

        val createRecordingsTable = """
            CREATE TABLE $TABLE_RECORDINGS (
                $COLUMN_RECORDING_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ANONYMOUS_ID TEXT,
                $COLUMN_GAME_ID TEXT,
                $COLUMN_TIMESTAMP INTEGER,
                FOREIGN KEY ($COLUMN_ANONYMOUS_ID) REFERENCES $TABLE_PATIENTS($COLUMN_ANONYMOUS_ID)
            )
        """
        db.execSQL(createRecordingsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PATIENTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RESULTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RECORDINGS")
        onCreate(db)
    }

    fun addPatient(name: String, surname: String, age: Int, gender: String, privacyAccepted: Boolean, logopedistaId: String): Long {
        val db = this.writableDatabase
        var uniqueId: String

        // Assicurarsi che l'ID univoco non esista già
        do {
            uniqueId = UUID.randomUUID().toString()
        } while (isUniqueIdExists(uniqueId))

        val contentValues = ContentValues().apply {
            put(COLUMN_ANONYMOUS_ID, uniqueId) // ID anonimo principale
            put(COLUMN_NAME, name)
            put(COLUMN_SURNAME, surname)
            put(COLUMN_AGE, age)
            put(COLUMN_GENDER, gender)
            put(COLUMN_PRIVACY_ACCEPTED, if (privacyAccepted) 1 else 0)
            put(COLUMN_LOGOPEDISTA_ID, logopedistaId)
        }
        return db.insert(TABLE_PATIENTS, null, contentValues)
    }

    private fun isUniqueIdExists(uniqueId: String): Boolean {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_PATIENTS,
            arrayOf(COLUMN_ANONYMOUS_ID),
            "$COLUMN_ANONYMOUS_ID = ?",
            arrayOf(uniqueId),
            null,
            null,
            null
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getAllPatients(): List<Patient> {
        val patients = mutableListOf<Patient>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_PATIENTS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val idAnonimo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANONYMOUS_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val surname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURNAME))
                val age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
                val gender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENDER))
                val privacyAccepted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PRIVACY_ACCEPTED)) == 1

                val patient = Patient(idAnonimo, name, surname, age, gender, privacyAccepted)
                patients.add(patient)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return patients
    }

    fun deletePatient(patientId: String) {
        val db = this.writableDatabase
        db.delete(TABLE_PATIENTS, "$COLUMN_ANONYMOUS_ID = ?", arrayOf(patientId))
    }

    fun saveGameResult(idUtente: String, idMinigioco: String, jsonData: String, metadata: String): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("id_utente", idUtente)
            put("id_minigioco", idMinigioco)
            put("json_data", jsonData)
            put("metadata", metadata)
        }
        return db.insert(TABLE_RESULTS, null, contentValues)
    }
}


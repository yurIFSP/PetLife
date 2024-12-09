package com.example.petlife

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PetLifeDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE pets (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                birthDate TEXT NOT NULL,
                type TEXT NOT NULL,
                color TEXT NOT NULL,
                size TEXT NOT NULL,
                lastVetVisit TEXT,
                lastVaccination TEXT,
                lastPetShopVisit TEXT,
                clinicPhone TEXT,
                clinicWebsite TEXT
            )
            """
        )

        db.execSQL(
            """
            CREATE TABLE events (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                date TEXT NOT NULL,
                pet_id INTEGER NOT NULL,
                FOREIGN KEY(pet_id) REFERENCES pets(id) ON DELETE CASCADE
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS pets")
        db.execSQL("DROP TABLE IF EXISTS events")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "PetLife.db"
        const val DATABASE_VERSION = 1
    }
}

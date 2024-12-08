package com.example.petlife

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PetLifeDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE pets (id INTEGER PRIMARY KEY, name TEXT, type TEXT)")
        db.execSQL("CREATE TABLE events (id INTEGER PRIMARY KEY, type TEXT, date TEXT, pet_id INTEGER, FOREIGN KEY(pet_id) REFERENCES pets(id))")
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

package com.example.petlife

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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
            size TEXT NOT NULL
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

    fun updatePet(pet: Pet): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("name", pet.name)
            put("birthDate", pet.birthDate)
            put("type", pet.type)
            put("color", pet.color)
            put("size", pet.size)
        }

        val rowsAffected = db.update(
            "pets",
            contentValues,
            "id = ?",
            arrayOf(pet.id.toString())
        )
        db.close()
        return rowsAffected > 0
    }

    fun getAllPets(): List<Pet> {
        val petList = mutableListOf<Pet>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM pets", null)

        if (cursor.moveToFirst()) {
            do {
                val pet = Pet(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    birthDate = cursor.getString(cursor.getColumnIndexOrThrow("birthDate")),
                    type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                    color = cursor.getString(cursor.getColumnIndexOrThrow("color")),
                    size = cursor.getString(cursor.getColumnIndexOrThrow("size")),
                )
                petList.add(pet)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return petList
    }

    fun deletePet(id: Int): Boolean {
        val db = writableDatabase
        val rowsDeleted = db.delete("pets", "id = ?", arrayOf(id.toString()))
        db.close()
        return rowsDeleted > 0
    }

    fun insertPet(pet: Pet): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("name", pet.name)
            put("birthDate", pet.birthDate)
            put("type", pet.type)
            put("color", pet.color)
            put("size", pet.size)
        }

        val id = db.insert("pets", null, contentValues)
        db.close()
        return id
    }

    companion object {
        const val DATABASE_NAME = "PetLife.db"
        const val DATABASE_VERSION = 1
    }
}

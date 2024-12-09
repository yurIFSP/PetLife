package com.example.petlife

import android.content.ContentValues
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
                size TEXT NOT NULL,
                color TEXT NOT NULL
            )
            """
        )

        // Criação da tabela de eventos
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

    // ----------------------- Métodos para Pets -----------------------

    // Insere um novo pet no banco
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

    // Retorna todos os pets cadastrados
    fun getAllPets(): List<Pet> {
        val petList = mutableListOf<Pet>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM pets", null)

        if (cursor.moveToFirst()) {
            do {
                val pet = Pet(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    birthDate = cursor.getString(cursor.getColumnIndexOrThrow("birthDate")),
                    type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                    size = cursor.getString(cursor.getColumnIndexOrThrow("size")),
                    color = cursor.getString(cursor.getColumnIndexOrThrow("color")) // Incluindo "color"
                )
                petList.add(pet)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return petList
    }

    // Atualiza os dados de um pet
    fun updatePet(pet: Pet): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("name", pet.name)
            put("birthDate", pet.birthDate)
            put("type", pet.type)
            put("size", pet.size)
            put("color", pet.color)
        }

        val rowsAffected = db.update("pets", contentValues, "id = ?", arrayOf(pet.id.toString()))
        db.close()
        return rowsAffected > 0
    }

    // Remove um pet do banco
    fun deletePet(petId: Int): Boolean {
        val db = writableDatabase
        val rowsDeleted = db.delete("pets", "id = ?", arrayOf(petId.toString()))
        db.close()
        return rowsDeleted > 0
    }

    // ----------------------- Métodos para Eventos -----------------------

    // Insere um novo evento
    fun insertEvent(event: Event): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("type", event.eventType)
            put("date", event.eventDate)
            put("pet_id", event.petId)
        }
        val id = db.insert("events", null, contentValues)
        db.close()
        return id
    }

    // Retorna todos os eventos relacionados a um pet
    fun getEventsByPetId(petId: Int): List<Event> {
        val eventList = mutableListOf<Event>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM events WHERE pet_id = ?", arrayOf(petId.toString()))

        if (cursor.moveToFirst()) {
            do {
                val event = Event(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    petId = cursor.getInt(cursor.getColumnIndexOrThrow("pet_id")),
                    eventType = cursor.getString(cursor.getColumnIndexOrThrow("type")),
                    eventDate = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                )
                eventList.add(event)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return eventList
    }

    // Remove um evento
    fun deleteEvent(eventId: Int): Boolean {
        val db = writableDatabase
        val rowsDeleted = db.delete("events", "id = ?", arrayOf(eventId.toString()))
        db.close()
        return rowsDeleted > 0
    }

    // Atualiza os dados de um evento
    fun updateEvent(event: Event): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("type", event.eventType)
            put("date", event.eventDate)
            put("pet_id", event.petId)
        }
        val rowsAffected = db.update("events", contentValues, "id = ?", arrayOf(event.id.toString()))
        db.close()
        return rowsAffected > 0
    }

    companion object {
        const val DATABASE_NAME = "PetLife.db"
        const val DATABASE_VERSION = 1
    }
}

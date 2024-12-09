package com.example.petlife

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class EventListActivity : ComponentActivity() {

    private lateinit var pet: Pet
    private val eventList = mutableStateListOf<Event>()

    private val addEventLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newEvent = result.data?.getParcelableExtra<Event>("event")
            if (newEvent != null) {
                val database = PetLifeDatabase(this)
                database.insertEvent(newEvent)
                loadEvents()
            }
        }
    }

    private val editEventLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val updatedEvent = result.data?.getParcelableExtra<Event>("event")
            if (updatedEvent != null) {
                val database = PetLifeDatabase(this)
                database.updateEvent(updatedEvent)
                loadEvents()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pet = intent.getParcelableExtra("pet") ?: run {
            finish()
            return
        }
        loadEvents()

        setContent {
            EventListScreen()
        }
    }

    private fun loadEvents() {
        val database = PetLifeDatabase(this)
        eventList.clear()
        val events = database.getEventsByPetId(pet.id)
        eventList.addAll(events)
        Log.d("EventListActivity", "Loaded ${events.size} events for pet_id=${pet.id}")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EventListScreen() {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Eventos de ${pet.name}") })
            },
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Text(
                        text = "Lista de Eventos",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyColumn(modifier = Modifier.weight(1f)) { // Permite que a lista ocupe espaço proporcional
                        if (eventList.isEmpty()) {
                            item {
                                Text(
                                    text = "Nenhum evento encontrado.",
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else {
                            items(eventList.size) { index ->
                                val event = eventList[index]
                                EventItem(
                                    event = event,
                                    onEdit = { editEvent(event) },
                                    onDelete = { removeEvent(event) }
                                )
                            }
                        }
                    }
                    Button(
                        onClick = { addEvent() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Adicionar Evento")
                    }
                }
            }
        )
    }

    @Composable
    fun EventItem(event: Event, onEdit: () -> Unit, onDelete: () -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Categoria: ${event.eventCategory}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Descrição: ${event.eventType}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Data: ${event.eventDate}", style = MaterialTheme.typography.bodyMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onEdit) { Text("Editar") }
                    Button(onClick = onDelete) { Text("Remover") }
                }
            }
        }
    }

    private fun addEvent() {
        val intent = Intent(this, EditEventActivity::class.java).apply {
            putExtra("petId", pet.id)
        }
        addEventLauncher.launch(intent)
    }

    private fun editEvent(event: Event) {
        val intent = Intent(this, EditEventActivity::class.java).apply {
            putExtra("event", event)
        }
        editEventLauncher.launch(intent)
    }

    private fun removeEvent(event: Event) {
        val database = PetLifeDatabase(this)
        database.deleteEvent(event.id)
        loadEvents()
    }
}

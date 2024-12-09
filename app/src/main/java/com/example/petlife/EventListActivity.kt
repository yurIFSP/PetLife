package com.example.petlife


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class EventListActivity : ComponentActivity() {

    private lateinit var pet: Pet
    private val eventList = mutableStateListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pet = intent.getParcelableExtra("pet") ?: throw IllegalArgumentException("Pet não encontrado")

        eventList.addAll(
            listOf(
                Event(petId = 1, eventType = "Veterinário", eventDate = "01/10/2024"),
                Event(petId = 1, eventType = "Vacinação", eventDate = "15/09/2024")
            )
        )

        setContent {
            EventListScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun EventListScreen() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Eventos de ${pet.name}") }
                )
            },
            content = { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    Text(
                        text = "Lista de Eventos",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(eventList.size) { index ->
                            val event = eventList[index]
                            EventItem(
                                event = event,
                                onEdit = { editEvent(event) },
                                onDelete = { removeEvent(event) }
                            )
                        }
                    }
                    Button(
                        onClick = { addEvent() },
                        modifier = Modifier.padding(16.dp)
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
                Text(text = "Tipo: ${event.eventType}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Data: ${event.eventDate}", style = MaterialTheme.typography.bodyMedium)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = onEdit) {
                        Text("Editar")
                    }
                    Button(onClick = onDelete) {
                        Text("Remover")
                    }
                }
            }
        }
    }

    private fun addEvent() {
        val intent = Intent(this, EditEventActivity::class.java)
        intent.putExtra("petId", pet.id)
        startActivity(intent)
    }

    private fun editEvent(event: Event) {
        val intent = Intent(this, EditEventActivity::class.java)
        intent.putExtra("event", event)
        startActivity(intent)
    }

    private fun removeEvent(event: Event) {
        eventList.remove(event)
    }
}

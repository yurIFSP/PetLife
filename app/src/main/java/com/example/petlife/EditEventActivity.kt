package com.example.petlife

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class EditEventActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val event = intent.getParcelableExtra<Event>("event")
        val petId = intent.getIntExtra("petId", -1)

        setContent {
            EditEventScreen(event, petId)
        }
    }

    @Composable
    fun EditEventScreen(event: Event?, petId: Int) {
        var eventType by remember { mutableStateOf(event?.eventType ?: "") }
        var eventDate by remember { mutableStateOf(event?.eventDate ?: "") }

        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = eventType,
                onValueChange = { eventType = it },
                label = { Text("Tipo de Evento") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = eventDate,
                onValueChange = { eventDate = it },
                label = { Text("Data do Evento") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val newEvent = event?.copy(eventType = eventType, eventDate = eventDate)
                    ?: Event(petId = petId, eventType = eventType, eventDate = eventDate)

                val resultIntent = Intent()
                resultIntent.putExtra("event", newEvent)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }) {
                Text("Salvar")
            }
        }
    }
}

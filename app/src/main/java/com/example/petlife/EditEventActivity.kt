package com.example.petlife

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
        val eventCategories = listOf("Vacinação", "Petshop", "Veterinário")
        var selectedCategory by remember { mutableStateOf(event?.eventCategory ?: "") }
        var eventType by remember { mutableStateOf(event?.eventType ?: "") }
        var eventDate by remember { mutableStateOf(event?.eventDate ?: "") }

        Column(modifier = Modifier.padding(16.dp)) {
            DropdownMenuField(
                label = "Categoria do Evento",
                options = eventCategories,
                selectedOption = selectedCategory,
                onOptionSelected = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = eventType,
                onValueChange = { eventType = it },
                label = { Text("Descrição do Evento") },
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
                val newEvent = event?.copy(
                    eventCategory = selectedCategory,
                    eventType = eventType,
                    eventDate = eventDate
                ) ?: Event(
                    petId = petId,
                    eventCategory = selectedCategory,
                    eventType = eventType,
                    eventDate = eventDate
                )

                val resultIntent = Intent()
                resultIntent.putExtra("event", newEvent)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }) {
                Text("Salvar")
            }
        }
    }

    @Composable
    fun DropdownMenuField(
        label: String,
        options: List<String>,
        selectedOption: String,
        onOptionSelected: (String) -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }

        Column {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { /* O campo é preenchido pelo menu */ },
                label = { Text(label) },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }


}
